NDK=$ANDROID_NDK_ROOT
TOOLCHAIN=$NDK/toolchain_armv7_21_libcxx/
CROSS_PREFIX=$TOOLCHAIN/bin/arm-linux-androideabi-
SYSROOT=$TOOLCHAIN/sysroot
PREFIX=$(pwd)/android/
#OPTIMIZE_CFLAGS="-mfloat-abi=softfp -mfpu=vfpv3-d16 -marm -march=armv7-a -mthumb -D__thumb__"
OPTIMIZE_CFLAGS="-fPIC -O3 -march=armv7-a -mfpu=neon -mtune=generic-armv7-a -mfloat-abi=softfp -ftree-vectorize -ffast-math"
#OPTIMIZE_CFLAGS="-march=armv7-a"

#make clean
mkdir -p $PREFIX
./configure --prefix=$PREFIX \
--with-sysroot=$SYSROOT \
--host=arm-linux \
--enable-static \
--target=android \
--disable-shared \
CC="${CROSS_PREFIX}clang --sysroot=$SYSROOT" \
CXX="${CROSS_PREFIX}clang++ --sysroot=$SYSROOT" \
RANLIB="${CROSS_PREFIX}ranlib" \
AR="${CROSS_PREFIX}ar" \
STRIP="${CROSS_PREFIX}strip" \
NM="${CROSS_PREFIX}nm"
CFLAGS="$OPTIMIZE_CFLAGS --sysroot=$SYSROOT" \
CXXFLAGS="$OPTIMIZE_CFLAGS --sysroot=$SYSROOT"

make -j8
make install
cp android/lib/libmp3lame.a ../ffmpeg/third/lib/
cp android/include/lame/* ../ffmpeg/third/include/lame/
make clean
