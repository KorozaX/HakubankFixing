# Add project-specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /usr/share/proguard/proguard-android.txt

# You can specify your own ProGuard rules here.
# Refer to the ProGuard manual for details: http://proguard.sourceforge.net/manual/

# Keep the names of the classes that are used in XML layouts and other resources
-keepclassmembers class * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep the names of the classes that are used in JNI
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep the names of the classes that are used in reflection
-keepclassmembers class * {
    ***;
}

# Keep the names of the classes that are used in annotations
-keepattributes *Annotation*

# Keep the names of the classes that are used in serialization
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    private java.lang.Object readResolve();
    private java.lang.Object writeReplace();
}

# Keep the names of the classes that are used in custom views
-keepclassmembers class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Keep the names of the classes that are used in custom adapters
-keepclassmembers class * extends android.widget.BaseAdapter {
    public <init>(...);
}

# Keep the names of the classes that are used in custom services
-keepclassmembers class * extends android.app.Service {
    public <init>(...);
}

# Keep the names of the classes that are used in custom activities
-keepclassmembers class * extends android.app.Activity {
    public <init>(...);
}

# Keep the names of the classes that are used in custom fragments
-keepclassmembers class * extends android.app.Fragment {
    public <init>(...);
}

# Keep the names of the classes that are used in custom broadcast receivers
-keepclassmembers class * extends android.content.BroadcastReceiver {
    public <init>(...);
}

# Keep the names of the classes that are used in custom content providers
-keepclassmembers class * extends android.content.ContentProvider {
    public <init>(...);
}

# Keep the names of the classes that are used in custom intent services
-keepclassmembers class * extends android.app.IntentService {
    public <init>(...);
}

# Keep the names of the classes that are used in custom job services
-keepclassmembers class * extends android.app.job.JobService {
    public <init>(...);
}

# Keep the names of the classes that are used in custom application classes
-keepclassmembers class * extends android.app.Application {
    public <init>(...);
}
