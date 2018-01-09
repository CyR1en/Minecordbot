#!/usr/bin/env sh

# Simple script to automatically calls the gradle wrapper
# Benefits:
# * Allows the user to cancel the process (5s buffer)
# * Captures the terminal window on finish, allowing users to read through the build log.
# * Clears the terminal after it's completed.

echo "This script will attempt to build Minecordbot for you."
sleep 1s
echo "You have 5 seconds to cancel this script with CTRL+C before we start building."
sleep 5s
echo
echo "Starting to build Minecordbot."
sleep 1s
echo #Start the build script
sh gradlew.sh build
echo
echo "All done! If you had any errors, please check the documentation and/or report the issue on the Discord server."
echo
read -n 1 -s -p "Press any key to exit and clean up the screen"
clear
exit 0
