/*
 * Copyright 2010-2016 Grzegorz Slowikowski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.code.play.selenium.step;

public class VerifyNotEqualsStep
    extends AbstractSeleniumStep
{

    private StringSeleniumCommand innerCommand;

    private String expected;

    public VerifyNotEqualsStep( StringSeleniumCommand innerCommand, String expected )
    {
        this.innerCommand = innerCommand;
        this.expected = expected;
    }

    protected void doExecute()
        throws Exception
    {
        String innerCommandResult = innerCommand.getString();
        String xexpected = innerCommand.storedVars.fillValues( expected );
        xexpected = MultiLineHelper.brToNewLine( xexpected );
        boolean seleniumNotEqualsResult = EqualsHelper.seleniumNotEquals( xexpected, innerCommandResult );
        Verify.verifyTrue( "Actual value \"" + innerCommandResult + "\" did match \"" + xexpected + "\"",
                           seleniumNotEqualsResult );
    }

    public String toString()
    {
        String cmd = innerCommand.command.substring( "get".length() );

        StringBuffer buf = new StringBuffer();
        buf.append( "verifyNot" ).append( cmd ).append( "(" );
        if ( !"".equals( innerCommand.param1 ) )
        {
            buf.append( "'" ).append( innerCommand.param1 ).append( "', " );
        }
        buf.append( "'" ).append( expected ).append( "')" );
        return buf.toString();
    }

}
