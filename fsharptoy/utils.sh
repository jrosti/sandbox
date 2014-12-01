
function install {
    brew install mono
    brew install fsharp
}

function nuget-all() {
    for package in FSharp.Core MongoDB.Driver Nancy Nancy.Hosting.Self; do
	./nuget install $package
    done
}

function compile() {
    RLIBS=$(find . -name \*.dll |grep net40 | while read dll; do echo "-r $dll "; done)
    echo $RLIBS
    cp $(find . -name FSharp\*.dll) /usr/local/Cellar/mono/3.4.0/lib/
    fsharpc $RLIBS hello.fs
    mkbundle --deps -c -oo hello.o -o hello.c hello.exe $(find . -name \*.dll |grep net40)
    gcc -I/usr/local/Cellar/mono/3.4.0/include/mono-2.0/ -L/usr/local/Cellar/mono/3.4.0/lib -lmonosgen-2.0  -framework Foundation -arch x86_64 -o hello hello.c hello.o
}
