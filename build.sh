#!/usr/bin/env sh

# Simple script to automatically calls the gradle wrapper
# Not entirely necessary, but helps users build without touching the CLI
echo "This script will attempt to build MineCordBot for you. Please make sure you have ran BuildTools, from SpigotMC."
echo
echo "You have 5 seconds to cancel this script with CTRL+C before we start building."
sleep 5s
echo
echo "Building with Gradle"
echo #Start the build script
sh gradlew.sh build
echo
echo "All done! If you had any errors, please check the documentation and/or report the issue on the Discord server."
echo
read -n 1 -s -p "Press any key to continue"
