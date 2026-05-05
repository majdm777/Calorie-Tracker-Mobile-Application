package com.example.dayone_calorietracker.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.dayone_calorietracker.R;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScanFragment extends Fragment {

    Button btnScan, btnGallery;
    private static final int PERMISSION_CAMERA_CODE = 100;
    private static final int PERMISSION_GALLERY_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int GALLERY_REQUEST_CODE = 201;

    public ScanFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        btnScan    = view.findViewById(R.id.Scan);
        btnGallery = view.findViewById(R.id.Gallery);

        btnScan.setOnClickListener(v -> checkCameraPermission());
        btnGallery.setOnClickListener(v -> checkGalleryPermission());

        return view;
    }

    // ─────────────────────────────────────────
    // PERMISSION CHECKS
    // ─────────────────────────────────────────

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSION_CAMERA_CODE);
        }
    }

    private void checkGalleryPermission() {
        String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                ? Manifest.permission.READ_MEDIA_IMAGES
                : Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(requireContext(), permission)
                == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            requestPermissions(
                    new String[]{permission},
                    PERMISSION_GALLERY_CODE);
        }
    }

    // ─────────────────────────────────────────
    // PERMISSION RESULT
    // ─────────────────────────────────────────

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean granted = grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED;

        if (requestCode == PERMISSION_CAMERA_CODE) {
            if (granted) {
                openCamera();
            } else {
                handleDenied(Manifest.permission.CAMERA,
                        "Camera permission is required to scan nutrition labels.");
            }

        } else if (requestCode == PERMISSION_GALLERY_CODE) {
            if (granted) {
                openGallery();
            } else {
                String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                        ? Manifest.permission.READ_MEDIA_IMAGES
                        : Manifest.permission.READ_EXTERNAL_STORAGE;
                handleDenied(permission,
                        "Storage permission is required to pick images from gallery.");
            }
        }
    }

    private void handleDenied(String permission, String message) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Permission Required")
                    .setMessage(message + "\nPlease enable it from app settings.")
                    .setPositiveButton("Go to Settings", (dialog, which) -> {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        // ✅ Fixed: use requireActivity().getPackageName() instead of getPackageName()
                        intent.setData(Uri.fromParts("package", requireActivity().getPackageName(), null));
                        startActivity(intent);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        } else {
            // ✅ Fixed: use requireContext() instead of `this` for Toast in a Fragment
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    // ─────────────────────────────────────────
    // OPEN CAMERA / GALLERY
    // ─────────────────────────────────────────

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    // ─────────────────────────────────────────
    // ACTIVITY RESULT
    // ─────────────────────────────────────────

    // ✅ Fixed: removed `protected` — Fragment uses public, not protected
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != getActivity().RESULT_OK || data == null) return;

        Bitmap bitmap = null;

        try {
            if (requestCode == CAMERA_REQUEST_CODE) {
                bitmap = (Bitmap) data.getExtras().get("data");

            } else if (requestCode == GALLERY_REQUEST_CODE) {
                Uri imageUri = data.getData();
                // ✅ Fixed: use requireActivity().getContentResolver() instead of getContentResolver()
                bitmap = MediaStore.Images.Media.getBitmap(
                        requireActivity().getContentResolver(), imageUri);
            }
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
        }

        if (bitmap != null) {
            scanNutritionLabel(bitmap);
        }
    }

    // ─────────────────────────────────────────
    // OCR — ML KIT
    // ─────────────────────────────────────────

    private void scanNutritionLabel(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(image)
                .addOnSuccessListener(visionText -> {
                    String extractedText = visionText.getText();
                    if (extractedText.isEmpty()) {
                        Toast.makeText(requireContext(), "No text found in image",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        parseNutritionInfo(extractedText);
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(), "Scan failed: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show()
                );
    }

    // ─────────────────────────────────────────
    // PARSE NUTRITION TEXT
    // ─────────────────────────────────────────

    private void parseNutritionInfo(String text) {
        text = text.toLowerCase();

        String[] lines = text.split("\n");

        double calories = 0, protein = 0, carbs = 0, fat = 0, sugar = 0;

        for (String line : lines) {

            // remove extra spaces
            line = line.trim();

            // skip empty lines
            if (line.isEmpty()) continue;

            if (contains(line, "calories", "السعرات")) {
                calories = extractFirstNumber(line);
            }

            else if (contains(line, "protein", "proteins", "بروتين")) {
                protein = extractFirstNumber(line);
            }

            else if (contains(line, "carbohydrate", "glucides", "كربوهيدرات")) {
                carbs = extractFirstNumber(line);
            }

            else if (contains(line, "fat", "lipides", "دهون")) {
                // avoid "saturated fat"
                if (!line.contains("saturated")) {
                    fat = extractFirstNumber(line);
                }
            }

            else if (contains(line, "sugar", "sucre", "سكر")) {
                sugar = extractFirstNumber(line);
            }
        }

        //pass these values to your confirmation fragment via Bundle or ViewModel
        Bundle bundle = new Bundle();
        bundle.putString("Action","Confirm");
        bundle.putDouble("calories", calories);
        bundle.putDouble("protein",  protein);
        bundle.putDouble("carbs",    carbs);
        bundle.putDouble("fat",      fat);
        bundle.putDouble("sugar",    sugar);


        NavHostFragment.findNavController(this)
                .navigate(R.id.action_scanFragment_to_addMealFragment, bundle);



        // Example navigation — replace with your actual fragment/nav graph
        // ConfirmNutritionFragment confirmFragment = new ConfirmNutritionFragment();
        // confirmFragment.setArguments(bundle);
        // requireActivity().getSupportFragmentManager().beginTransaction()
        //         .replace(R.id.fragment_container, confirmFragment)
        //         .addToBackStack(null)
        //         .commit();
    }

    private int extractValue(String text, String pattern) {
        Matcher m = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(text);
        if (m.find()) {
            try { return Integer.parseInt(m.group(1)); } catch (Exception ignored) {}
        }
        return 0;
    }
    private boolean contains(String line, String... keywords) {
        for (String k : keywords) {
            if (line.contains(k)) return true;
        }
        return false;
    }

    private double extractFirstNumber(String line) {
        Matcher m = Pattern.compile("(\\d+\\.?\\d*)").matcher(line);
        if (m.find()) {
            try {
                return Double.parseDouble(m.group(1));
            } catch (Exception ignored) {}
        }
        return 0;
    }
}
    // ─────────────────────────────────────────
    // SAVE — plug into your own DB/logic here
    // ─────────────────────────────────────────




