package com.example.family112;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class ImportXlsxService extends Service {
    public ImportXlsxService() {
    }

    private ImportXlsxBinder importXlsxBinder;

    @Override
    public void onCreate(){
        super.onCreate();
        importXlsxBinder = new ImportXlsxBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (importXlsxBinder != null)
            return importXlsxBinder;
        return null;
    }

    class ImportXlsxBinder extends Binder{
        public ArrayList<StudentInfo> readXlsx(){
            ArrayList<StudentInfo> studentInfos = new ArrayList<>();

            AssetManager assetManager = getResources().getAssets();
            Workbook workbook;
            Sheet sheet;
            Cell U_ID, U_NAME, U_CITY, U_UNIVERCITY, U_LONGITUDE, U_LATITUDE;
            try{
                workbook = Workbook.getWorkbook(assetManager.open("112.xlsx"));
                sheet = workbook.getSheet(0);
                for(int row = 2; row < 59;row++){
                    U_ID = sheet.getCell(0, row);
                    U_NAME = sheet.getCell(1, row);
                    U_CITY = sheet.getCell(2,row);
                    U_UNIVERCITY = sheet.getCell(3, row);
                    U_LONGITUDE =sheet.getCell(5,row);
                    U_LATITUDE = sheet.getCell(6,row);

                    StudentInfo studentInfo = new StudentInfo(
                            Integer.parseInt(U_ID.getContents()) - 11200,
                            Integer.parseInt(U_ID.getContents()),
                            U_NAME.getContents(),
                            U_CITY.getContents(),
                            U_UNIVERCITY.getContents(),
                            Double.parseDouble(U_LONGITUDE.getContents()),
                            Double.parseDouble(U_LATITUDE.getContents())
                    );
                    studentInfos.add(studentInfo);
                }
            }catch (Exception e){

            }

            return studentInfos;
        }
    }
}