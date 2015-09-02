@REM
@REM This file is part of RedstoneLamp.
@REM
@REM RedstoneLamp is free software: you can redistribute it and/or modify
@REM it under the terms of the GNU Lesser General Public License as published by
@REM the Free Software Foundation, either version 3 of the License, or
@REM (at your option) any later version.
@REM
@REM RedstoneLamp is distributed in the hope that it will be useful,
@REM but WITHOUT ANY WARRANTY; without even the implied warranty of
@REM MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
@REM GNU Lesser General Public License for more details.
@REM
@REM You should have received a copy of the GNU Lesser General Public License
@REM along with RedstoneLamp.  If not, see <http://www.gnu.org/licenses/>.
@REM

:: This simple batch file is to fix eclipse dependency errors, it downloads
:: The maven dependencies and adds them to the classpath for eclipse

mvn eclipse:eclipse
echo "Finished refresh of dependencies!"
pause
