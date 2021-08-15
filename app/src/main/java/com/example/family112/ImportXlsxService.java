package com.example.family112;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
            Log.i("ImportXlsxService","In readXlsx");
            ArrayList<StudentInfo> studentInfos = new ArrayList<>();

            AssetManager assetManager = getResources().getAssets();
            InputStreamReader is = null;
            try{
                is = new InputStreamReader(assetManager.open("112.csv"));
                BufferedReader reader = new BufferedReader(is);
                reader.readLine();
                String line;
                Integer cnt = Integer.valueOf(0);
                while((line = reader.readLine())!= null){
                    int id = 0, number = 0;
                    double longitude = 0.0, latitude = 0.0;
                    String name = "", city = "", university = "", major = "";
                    for(String retval: line.split(",")){
                        switch (cnt){
                            case 0:
                                number = Integer.parseInt(retval);
                                id = number - 11200;
                                break;
                            case 1:
                                name = retval;
                                break;
                            case 2:
                                city = retval;
                                break;
                            case 3:
                                university = retval;
                                break;
                            case 4:
                                major = retval;
                                break;
                            case 5:
                                longitude = Double.parseDouble(retval);
                                break;
                            case 6:
                                latitude = Double.parseDouble(retval);
                                break;
                            default:
                                break;
                        }
                        cnt += 1;
                    }
                    StudentInfo studentInfo = new StudentInfo(id, number, name, city, university, longitude, latitude);
                    studentInfos.add(studentInfo);
                    cnt = 0;
                }

            }catch (Exception e){
                Log.e("ImportXlsxService","Error",e);
            }

            return studentInfos;
        }
    }
}