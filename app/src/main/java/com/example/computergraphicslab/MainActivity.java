package com.example.computergraphicslab;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;



public class MainActivity extends AppCompatActivity implements CustomMatrixDialog.DialogListener {
    private int REQUEST_CODE = 1;
    private ViewPager myPager;
    private TextView height_width;
    private Spinner spinner;
    private Button btnSave;
    int position = 0;
    float div = 0;
    private ArrayList<Bitmap> historyList = new ArrayList<Bitmap>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        myPager = findViewById(R.id.image_view);
        ViewPagerAdapter adapterPager = new ViewPagerAdapter(this, historyList);
        myPager.setAdapter(adapterPager);
        myPager.setCurrentItem(0);

        myPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                position = i;
                height_width.setText("Height = " + ViewPagerAdapter.getmCurrentBitmap(position).getHeight() + "\t \t \t  X  \t \t \t"
                        +"Width = " + ViewPagerAdapter.getmCurrentBitmap(position).getWidth());

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        height_width = findViewById(R.id.image_height_width);
        spinner = findViewById(R.id.spinner);
        btnSave = findViewById(R.id.btn_save);
            spinner.setVisibility(View.INVISIBLE);
            btnSave.setVisibility(View.INVISIBLE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_image_gallery_10dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_CODE);
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.settings, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (parent.getSelectedItemPosition()) {
                    case 1: {
                        blackAndWhiteImageMaker();
                        spinner.setSelection(0);
                        myPager.setCurrentItem(historyList.size());
                        break;
                    }
                    case 2: {
                        float[][] filterSharpness = new float[][]{
                                {-1, -1, -1},
                                {-1, 9, -1},
                                {-1, -1, -1},
                        };

                        imageCustomization(filterSharpness);
                        spinner.setSelection(0);
                        myPager.setCurrentItem(historyList.size());
                        break;
                    }
                    case 3: {
                        Toast.makeText(MainActivity.this, "Blur", Toast.LENGTH_SHORT).show();
                        float[][] filterBlur = new float[][]{
                                {1, 1, 1},
                                {1, 1, 1},
                                {1, 1, 1},
                        };

                        imageCustomization(filterBlur);
                        spinner.setSelection(0);
                        myPager.setCurrentItem(historyList.size());
                        break;
                    }
                    case 4: {
                        float[][] filterEmboss = new float[][]{
                                {-2, -1, 0},
                                {-1, 1, 1},
                                {0, 1, 2},
                        };

                        imageCustomization(filterEmboss);
                        spinner.setSelection(0);
                        myPager.setCurrentItem(historyList.size());
                        break;
                    }
                    case 5: {
                        openDialogFragment();
                        spinner.setSelection(0);

                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void imageCustomization(float[][] filter) {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                div += filter[i][j];
            }
        }

        Bitmap bitmap = (Bitmap) ViewPagerAdapter.getmCurrentBitmap(position);
        Bitmap mutableBitmap = bitmap.copy(bitmap.getConfig(), true);
        for (int x = 1; x < bitmap.getWidth() - 1; x++) {
            for (int y = 1; y < bitmap.getHeight() - 1; y++) {
                int color1 = bitmap.getPixel(x, y);
                int color2 = bitmap.getPixel(x - 1, y - 1);
                int color3 = bitmap.getPixel(x, y - 1);
                int color4 = bitmap.getPixel(x + 1, y - 1);
                int color5 = bitmap.getPixel(x - 1, y);
                int color6 = bitmap.getPixel(x + 1, y);
                int color7 = bitmap.getPixel(x - 1, y + 1);
                int color8 = bitmap.getPixel(x, y + 1);
                int color9 = bitmap.getPixel(x + 1, y + 1);

                int red = (int) ((Color.red(color1) * filter[1][1] + Color.red(color2) * filter[0][0] + Color.red(color3) * filter[1][0] + Color.red(color4) * filter[2][0]
                        + Color.red(color5) * filter[0][1] + Color.red(color6) * filter[0][2] + Color.red(color7) * filter[0][2] + Color.red(color8) * filter[1][2]
                        + Color.red(color9) * filter[2][2]) / div);
                int blue = (int) ((Color.blue(color1) * filter[1][1] + Color.blue(color2) * filter[0][0] + Color.blue(color3) * filter[1][0] + Color.blue(color4) * filter[2][0]
                        + Color.blue(color5) * filter[0][1] + Color.blue(color6) * filter[0][2] + Color.blue(color7) * filter[0][2] + Color.blue(color8) * filter[1][2]
                        + Color.blue(color9) * filter[2][2]) / div);
                int green = (int) ((Color.green(color1) * filter[1][1] + Color.green(color2) * filter[0][0] + Color.green(color3) * filter[1][0] + Color.green(color4) * filter[2][0]
                        + Color.green(color5) * filter[0][1] + Color.green(color6) * filter[0][2] + Color.green(color7) * filter[0][2] + Color.green(color8) * filter[1][2]
                        + Color.green(color9) * filter[2][2]) / div);

                if (red > 255) {
                    red = 255;
                }
                if (blue > 255) {
                    blue = 255;
                }
                if (green > 255) {
                    green = 255;
                }

                if (red < 0) {
                    red = 0;
                }
                if (blue < 0) {
                    blue = 0;
                }
                if (green < 0) {
                    green = 0;
                }

                int newRed = red;
                int newGreen = green;
                int newBlue = blue;

                mutableBitmap.setPixel(x, y, Color.rgb(newRed, newGreen, newBlue));

            }
        }
        historyList.add(mutableBitmap);
        ViewPagerAdapter adapterPager = new ViewPagerAdapter(this, historyList);
        myPager.setAdapter(adapterPager);
        div = 0;
    }

    public void openDialogFragment(){
        CustomMatrixDialog customMatrixDialog = new CustomMatrixDialog();
        customMatrixDialog.show(getSupportFragmentManager(),"Dialog");
    }

    @Override
    public void applyMatrix(float[][] matrix) {

        imageCustomization(matrix);
        myPager.setCurrentItem(historyList.size());
    }

    private void blackAndWhiteImageMaker() {
        Bitmap bitmap = (Bitmap) ViewPagerAdapter.getmCurrentBitmap(position);
        Bitmap mutableBitmap = bitmap.copy(bitmap.getConfig(), true);
        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                int pixel = (bitmap.getPixel(x, y));
                int blue = Color.blue(pixel);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int grey = (int) (red * 0.299 + green * 0.587 + blue * 0.114);

                int newRed = grey;
                int newGreen = grey;
                int newBlue = grey;

                mutableBitmap.setPixel(x, y, Color.rgb(newRed, newGreen, newBlue));
            }
        }
        historyList.add(mutableBitmap);
        ViewPagerAdapter adapterPager = new ViewPagerAdapter(this, historyList);
        myPager.setAdapter(adapterPager);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                historyList.add(bitmap);
                ViewPagerAdapter adapterPager = new ViewPagerAdapter(this, historyList);
                myPager.setAdapter(adapterPager);
                if (ViewPagerAdapter.getmCurrentBitmap(position) != null) {
                    spinner.setVisibility(View.VISIBLE);
                    btnSave.setVisibility(View.VISIBLE);
                }
                height_width.setText("Height = " + bitmap.getHeight() + "\t \t \t  X  \t \t \t Width = " + bitmap.getWidth());
            } catch (IOException io) {
                Toast.makeText(this, "IO Exception ERROR", Toast.LENGTH_SHORT).show();
            }
            myPager.setCurrentItem(historyList.size());
        }
    }


    public void btn_SaveImage(View view) {
        if (ViewPagerAdapter.getmCurrentBitmap(position) == null) {
            Toast.makeText(this, "Nothing to save", Toast.LENGTH_SHORT).show();
        } else {
            Bitmap image = ViewPagerAdapter.getmCurrentBitmap(position);
            File patch = Environment.getExternalStorageDirectory();
            File dir = new File(patch + "/Customized Images/");
            dir.mkdirs();
            File file = new File(dir, (UUID.randomUUID()) + ".jpg");
            OutputStream out = null;
            try {
                out = new FileOutputStream(file);
                image.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                Toast.makeText(this, "Image was saved", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}