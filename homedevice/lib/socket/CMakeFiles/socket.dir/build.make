# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 2.8

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:

# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list

# Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/bin/cmake

# The command to remove a file.
RM = /usr/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /root/battlehack_client

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /root/battlehack_client

# Include any dependencies generated for this target.
include lib/socket/CMakeFiles/socket.dir/depend.make

# Include the progress variables for this target.
include lib/socket/CMakeFiles/socket.dir/progress.make

# Include the compile flags for this target's objects.
include lib/socket/CMakeFiles/socket.dir/flags.make

lib/socket/CMakeFiles/socket.dir/socket.cpp.o: lib/socket/CMakeFiles/socket.dir/flags.make
lib/socket/CMakeFiles/socket.dir/socket.cpp.o: lib/socket/socket.cpp
	$(CMAKE_COMMAND) -E cmake_progress_report /root/battlehack_client/CMakeFiles $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building CXX object lib/socket/CMakeFiles/socket.dir/socket.cpp.o"
	cd /root/battlehack_client/lib/socket && /usr/bin/c++   $(CXX_DEFINES) $(CXX_FLAGS) -o CMakeFiles/socket.dir/socket.cpp.o -c /root/battlehack_client/lib/socket/socket.cpp

lib/socket/CMakeFiles/socket.dir/socket.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/socket.dir/socket.cpp.i"
	cd /root/battlehack_client/lib/socket && /usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -E /root/battlehack_client/lib/socket/socket.cpp > CMakeFiles/socket.dir/socket.cpp.i

lib/socket/CMakeFiles/socket.dir/socket.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/socket.dir/socket.cpp.s"
	cd /root/battlehack_client/lib/socket && /usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -S /root/battlehack_client/lib/socket/socket.cpp -o CMakeFiles/socket.dir/socket.cpp.s

lib/socket/CMakeFiles/socket.dir/socket.cpp.o.requires:
.PHONY : lib/socket/CMakeFiles/socket.dir/socket.cpp.o.requires

lib/socket/CMakeFiles/socket.dir/socket.cpp.o.provides: lib/socket/CMakeFiles/socket.dir/socket.cpp.o.requires
	$(MAKE) -f lib/socket/CMakeFiles/socket.dir/build.make lib/socket/CMakeFiles/socket.dir/socket.cpp.o.provides.build
.PHONY : lib/socket/CMakeFiles/socket.dir/socket.cpp.o.provides

lib/socket/CMakeFiles/socket.dir/socket.cpp.o.provides.build: lib/socket/CMakeFiles/socket.dir/socket.cpp.o

# Object files for target socket
socket_OBJECTS = \
"CMakeFiles/socket.dir/socket.cpp.o"

# External object files for target socket
socket_EXTERNAL_OBJECTS =

lib/socket/libsocket.a: lib/socket/CMakeFiles/socket.dir/socket.cpp.o
lib/socket/libsocket.a: lib/socket/CMakeFiles/socket.dir/build.make
lib/socket/libsocket.a: lib/socket/CMakeFiles/socket.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --red --bold "Linking CXX static library libsocket.a"
	cd /root/battlehack_client/lib/socket && $(CMAKE_COMMAND) -P CMakeFiles/socket.dir/cmake_clean_target.cmake
	cd /root/battlehack_client/lib/socket && $(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/socket.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
lib/socket/CMakeFiles/socket.dir/build: lib/socket/libsocket.a
.PHONY : lib/socket/CMakeFiles/socket.dir/build

lib/socket/CMakeFiles/socket.dir/requires: lib/socket/CMakeFiles/socket.dir/socket.cpp.o.requires
.PHONY : lib/socket/CMakeFiles/socket.dir/requires

lib/socket/CMakeFiles/socket.dir/clean:
	cd /root/battlehack_client/lib/socket && $(CMAKE_COMMAND) -P CMakeFiles/socket.dir/cmake_clean.cmake
.PHONY : lib/socket/CMakeFiles/socket.dir/clean

lib/socket/CMakeFiles/socket.dir/depend:
	cd /root/battlehack_client && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /root/battlehack_client /root/battlehack_client/lib/socket /root/battlehack_client /root/battlehack_client/lib/socket /root/battlehack_client/lib/socket/CMakeFiles/socket.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : lib/socket/CMakeFiles/socket.dir/depend

