.class public Output 
.super java/lang/Object

.method public <init>()V
 aload_0
 invokenonvirtual java/lang/Object/<init>()V
 return
.end method

.method public static print(I)V
 .limit stack 2
 getstatic java/lang/System/out Ljava/io/PrintStream;
 iload_0 
 invokestatic java/lang/Integer/toString(I)Ljava/lang/String;
 invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
 return
.end method

.method public static read()I
 .limit stack 3
 new java/util/Scanner
 dup
 getstatic java/lang/System/in Ljava/io/InputStream;
 invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V
 invokevirtual java/util/Scanner/next()Ljava/lang/String;
 invokestatic java/lang/Integer.parseInt(Ljava/lang/String;)I
 ireturn
.end method

.method public static run()V
 .limit stack 1024
 .limit locals 256
 ldc 0
 istore 0
 ldc 14
 istore 1
L1:
 iload 0
 ldc 5
 if_icmplt L2
 goto L3
L2:
 ldc 1
 iload 0
 iadd 
 istore 0
 goto L1
L3:
 ldc 0
 istore 0
L4:
 iload 0
 ldc 5
 if_icmplt L5
 goto L6
L5:
 ldc 1
 iload 0
 iadd 
 istore 0
 iload 0
 invokestatic Output/print(I)V
 goto L4
L6:
 invokestatic Output/read()I
 istore 2
 invokestatic Output/read()I
 istore 3
 invokestatic Output/read()I
 istore 4
 iload 2
 invokestatic Output/print(I)V
 iload 3
 invokestatic Output/print(I)V
 iload 4
 invokestatic Output/print(I)V
 iload 0
 ldc 6
 if_icmplt L7
 goto L8
L7:
 iload 0
 invokestatic Output/print(I)V
 goto L9
L8:
 goto L9
L9:
 iload 0
 ldc 6
 if_icmpgt L10
 goto L11
L10:
 ldc 1
 istore 1
 iload 1
 invokestatic Output/print(I)V
 goto L12
L11:
 ldc 70
 invokestatic Output/print(I)V
 ldc 98
 istore 5
 ldc 98
 iload 5
 isub 
 invokestatic Output/print(I)V
 goto L12
L12:
 goto L0
L0:
 return
.end method

.method public static main([Ljava/lang/String;)V
 invokestatic Output/run()V
 return
.end method

