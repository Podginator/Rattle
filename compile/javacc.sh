echo 'java -cp /Users/podginator/LanguageDesign/javacc-6.0/bin/lib/javacc.jar $(basename $0) "$@"' > javacc
chmod 755 javacc
ln -s javacc jjtree
ln -s javacc jjdoc