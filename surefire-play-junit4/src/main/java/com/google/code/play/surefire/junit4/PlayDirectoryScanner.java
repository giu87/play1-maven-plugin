/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.google.code.play.surefire.junit4;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.maven.surefire.report.ConsoleLogger;
import org.apache.maven.surefire.util.DirectoryScanner;
import org.apache.maven.surefire.util.NestedRuntimeException;
import org.apache.maven.surefire.util.ScannerFilter;
import org.apache.maven.surefire.util.TestsToRun;

/**
 * ...Scans directories looking for tests.
 * Based on Apache Maven Surefire's class DefaultDirectoryScanner
 * (http://svn.apache.org/repos/asf/maven/surefire/tags/surefire-2.9/surefire-api/src/main/java/org/apache/maven/surefire/util/DefaultDirectoryScanner.java)
 */
public class PlayDirectoryScanner
    implements DirectoryScanner
{

    private static final String FS = System.getProperty( "file.separator" );

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    //private static final String JAVA_SOURCE_FILE_EXTENSION = ".java";

    //private static final String JAVA_CLASS_FILE_EXTENSION = ".class";

    private final File basedir;

    private final List<String> includes;

    private final List<String> excludes;

    private final List<Class<?>> classesSkippedByValidation = new ArrayList<Class<?>>();

    private final Comparator<Class<?>> sortOrder;

    private final String runOrder;

    private final ConsoleLogger consoleLogger;


    public PlayDirectoryScanner( File basedir, List<String> includes, List<String> excludes, String runOrder, ConsoleLogger consoleLogger )
    {
        this.basedir = basedir;
        this.includes = includes;
        this.excludes = excludes;
        this.runOrder = runOrder;
        this.sortOrder = getSortOrderComparator( runOrder );
        this.consoleLogger = consoleLogger;
    }

    public TestsToRun locateTestClasses( ClassLoader classLoader, ScannerFilter scannerFilter )
    {
        String[] testClassNames = collectTests();
        List<Class<?>> result = new ArrayList<Class<?>>();

        for ( int i = 0; i < testClassNames.length; i++ )
        {
            String className = testClassNames[i];
            Class<?> testClass = loadClass( classLoader, className );
            if ( scannerFilter == null || scannerFilter.accept( testClass ) )
            {
                if ( testClass.getClassLoader() != classLoader )
                {
                    consoleLogger.info( String.format( "WARNING: Test class %s not loaded by Play.classloader. This may cause unexpected problems.\n",
                                                       testClass.getName() ) );
                }
                result.add( testClass );
            }
            else
            {
                classesSkippedByValidation.add( testClass );
            }
        }
        if ( "random".equals( runOrder ) )
        {
            Collections.shuffle( result );
        }
        else if ( sortOrder != null )
        {
            Collections.sort( result, sortOrder );
        }
        return new TestsToRun( result );
    }

    private /*???static*/ Class<?> loadClass( ClassLoader classLoader, String className )
    {
        Class<?> testClass;
        try
        {
            testClass = classLoader.loadClass( className );
        }
        catch ( ClassNotFoundException e )
        {
            throw new NestedRuntimeException( "Unable to create test class '" + className + "'", e );
        }
        return testClass;
    }


    String[] collectTests()
    {
        String[] tests = EMPTY_STRING_ARRAY;
        if ( basedir.exists() )
        {
            org.codehaus.plexus.util.DirectoryScanner scanner = new org.codehaus.plexus.util.DirectoryScanner();

            scanner.setBasedir( basedir );

            if ( includes != null )
            {
                scanner.setIncludes( processIncludesExcludes( includes ) );
            }

            if ( excludes != null )
            {
                scanner.setExcludes( processIncludesExcludes( excludes ) );
            }

            scanner.scan();

            tests = scanner.getIncludedFiles();
            for ( int i = 0; i < tests.length; i++ )
            {
                String test = tests[i];
                test = test.substring( 0, test.indexOf( "." ) );
                tests[i] = test.replace( FS.charAt( 0 ), '.' );
            }
        }
        return tests;
    }

    private /*???static*/ String[] processIncludesExcludes( List<String> list )
    {
        String[] incs = new String[list.size()];

        for ( int i = 0; i < incs.length; i++ )
        {
            String inc = (String) list.get( i );
            /*if ( inc.endsWith( JAVA_SOURCE_FILE_EXTENSION ) )
            {
                inc = new StringBuffer(
                    inc.length() - JAVA_SOURCE_FILE_EXTENSION.length() + JAVA_CLASS_FILE_EXTENSION.length() ).append(
                    inc.substring( 0, inc.lastIndexOf( JAVA_SOURCE_FILE_EXTENSION ) ) ).append(
                    JAVA_CLASS_FILE_EXTENSION ).toString();
            }*/
            incs[i] = inc;

        }
        return incs;
    }

    public List<Class<?>> getClassesSkippedByValidation()
    {
        return classesSkippedByValidation;
    }

    private Comparator<Class<?>> getSortOrderComparator( String runOrder )
    {
        if ( "alphabetical".equals( runOrder ) )
        {
            return getAlphabeticalComparator();
        }

        else if ( "reversealphabetical".equals( runOrder ) )
        {
            return getReverseAlphabeticalComparator();
        }
        else if ( "hourly".equals( runOrder ) )
        {
            final int hour = Calendar.getInstance().get( Calendar.HOUR_OF_DAY );
            return ( ( hour % 2 ) == 0 )
                ? getAlphabeticalComparator()
                : getReverseAlphabeticalComparator();
        }
        return null;
    }

    private Comparator<Class<?>> getReverseAlphabeticalComparator()
    {
        return new Comparator<Class<?>>()
        {
            public int compare( Class<?> o1, Class<?> o2 )
            {
                return ( o2.getName().compareTo( o1.getName() ) );
            }
        };
    }

    private Comparator<Class<?>> getAlphabeticalComparator()
    {
        return new Comparator<Class<?>>()
        {
            public int compare( Class<?> o1, Class<?> o2 )
            {
                return ( o1.getName().compareTo( o2.getName() ) );
            }
        };
    }

}