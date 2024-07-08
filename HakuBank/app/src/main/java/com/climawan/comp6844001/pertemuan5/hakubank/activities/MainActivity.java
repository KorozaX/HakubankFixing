public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isDeviceRooted() || isEmulator()) {
            // Handle rooted or emulated device
            Toast.makeText(this, "This device is not supported. The application may not work properly.", Toast.LENGTH_LONG).show();
            finish(); // Close the app if rooted or emulated device detected
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!isDeviceRooted() && !isEmulator()) {
            startActivity(
                    new Intent(this, LoginActivity.class)
            );
        }
    }

    private boolean isDeviceRooted() {
        // Check for test-keys
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }

        // Check for Superuser APK
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Check for SU binary
        return canExecuteCommand("/system/xbin/which su") || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su");
    }

    private boolean canExecuteCommand(String command) {
        boolean executedSuccessfully;
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) {
                executedSuccessfully = true;
            } else {
                executedSuccessfully = false;
            }
            process.destroy();
        } catch (Exception e) {
            executedSuccessfully = false;
        }
        return executedSuccessfully;
    }

    private boolean isEmulator() {
        String fingerprint = android.os.Build.FINGERPRINT;
        String model = android.os.Build.MODEL;
        String manufacturer = android.os.Build.MANUFACTURER;
        String brand = android.os.Build.BRAND;
        String device = android.os.Build.DEVICE;
        String product = android.os.Build.PRODUCT;

        return fingerprint.startsWith("generic")
                || fingerprint.startsWith("unknown")
                || model.contains("google_sdk")
                || model.contains("Emulator")
                || model.contains("Android SDK built for x86")
                || manufacturer.contains("Genymotion")
                || (brand.startsWith("generic") && device.startsWith("generic"))
                || "google_sdk".equals(product);
    }
}
