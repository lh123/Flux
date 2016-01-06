# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keep class com.tencent.bugly.**{*;}
# LeakCanary
-keep class org.eclipse.mat.** { *; }
-dontwarn com.squareup.leakcanary.**
-keep class com.squareup.leakcanary.** { *; }

-dontwarn com.umeng.update.**
-keep class com.umeng.update.**{*;}
-dontwarn u.upd.g
-keep class u.upd.g{*;}
#otto
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}

-keep class com.lh.flux.model.entity.**{*;}
##---------------Begin: proguard configuration for fastjson  ----------

#-keepnames class * implements java.io.Serializable
-keep public class * implements java.io.Serializable {
        public *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-dontskipnonpubliclibraryclassmembers
-dontskipnonpubliclibraryclasses
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }
-libraryjars libs/fastjson.jar

-keepclassmembers class * {
public <methods>;
}