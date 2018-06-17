


#SET THE PATH DEPENDS ON YOUR DIERECTORY (PATH TO YOUR PROJECT + OPEN CV SDK):
set(pathToProject /home/praga/Zouhir_PFE/OpenCv)
set(pathToOpenCv /home/praga/opencv3-android-sdk-with-contrib-master/OpenCV-android-sdk)



cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_VERBOSE_MAKEFILE on)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

include_directories(${pathToOpenCv}/sdk/native/jni/include)


add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/native-lib.cpp )

add_library( lib_opencv SHARED IMPORTED )



find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )



set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${pathToProject}/app/src/main/jniLibs/${ANDROID_ABI}/libopencv_java3.so)

target_link_libraries( # Specifies the target library.
                       native-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib}
                       lib_opencv )