package tet;


import com.esri.arcgis.datasourcesfile.ShapefileWorkspaceFactory;
import com.esri.arcgis.geodatabase.*;
import com.esri.arcgis.geometry.*;
import com.esri.arcgis.system.AoInitialize;
import com.esri.arcgis.system.EngineInitializer;
import com.esri.arcgis.system.esriLicenseProductCode;
import com.esri.arcgis.system.esriLicenseStatus;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;


public class sdad {
    public sdad() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        InitLiscense();
        EngineInitializer.initializeEngine();
        try {

            com.esri.arcgis.system.AoInitialize ao = new com.esri.arcgis.system.AoInitialize();

            if (ao.isProductCodeAvailable(com.esri.arcgis.system.esriLicenseProductCode.esriLicenseProductCodeEngineGeoDB) == com.esri.arcgis.system.esriLicenseStatus.esriLicenseAvailable)

                ao.initialize(com.esri.arcgis.system.esriLicenseProductCode.esriLicenseProductCodeEngineGeoDB);

        } catch (Exception e) {

            e.printStackTrace();

        }
//        EngineInitializer.initializeVisualBeans();
//        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        initializeArcGISLicenses();
    }
    public static void InitLiscense() {
        String arcObjectsHome = "C:\\Program Files (x86)\\ArcGIS\\Desktop10.2\\";

        // If no runtime is available, exit application gracefully
        if (arcObjectsHome == null) {
            System.err
                    .println("You must have the ArcGIS Engine Runtime installed in order to execute this application.");
            System.err
                    .println("Install the product above, then re-run this application.");
            System.err.println("Exiting execution of this application...");
            System.exit(-1);
        }

        // Obtain the relative path to the arcobjects.jar file
        String jarPath = arcObjectsHome + "java" + File.separator + "lib"
                + File.separator + "arcobjects.jar";

// Create a new file
        File jarFile = new File(jarPath);
// Helps load classes and resources from a search path of URLs
        URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class sysclass = URLClassLoader.class;
        try {
            Method method = sysclass.getDeclaredMethod("addURL",
                    new Class[] { URL.class });
            method.setAccessible(true);
            method.invoke(sysloader, new Object[] { jarFile.toURI().toURL() });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.err
                    .println("Could not add arcobjects.jar to system classloader");
            System.err.println("Exiting execution of this application...");
            System.exit(-1);
        }
    }

    public static void initializeArcGISLicenses() {
        try {
            AoInitialize ao = new AoInitialize();
            if (ao.isProductCodeAvailable(esriLicenseProductCode.esriLicenseProductCodeEngine)
                    == esriLicenseStatus.esriLicenseAvailable)
                ao.initialize(esriLicenseProductCode.esriLicenseProductCodeEngine);
            else if (ao.isProductCodeAvailable(esriLicenseProductCode.esriLicenseProductCodeBasic)
                    == esriLicenseStatus.esriLicenseAvailable)
                ao.initialize(esriLicenseProductCode.esriLicenseProductCodeBasic);
            else if (ao.isProductCodeAvailable(esriLicenseProductCode.esriLicenseProductCodeStandard)
                    == esriLicenseStatus.esriLicenseAvailable)
                ao.initialize(esriLicenseProductCode.esriLicenseProductCodeStandard);
            else if (ao.isProductCodeAvailable(esriLicenseProductCode.esriLicenseProductCodeAdvanced)
                    == esriLicenseStatus.esriLicenseAvailable)
                ao.initialize(esriLicenseProductCode.esriLicenseProductCodeAdvanced);
        } catch (Exception e) {e.printStackTrace();}
    }

    public void createPolygon( String xFolder,String xShapeName,String xlsxFolder) throws IOException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {

        String xjFolder = xFolder;
        String xjShapeName = xShapeName;
        IWorkspaceFactory xjWsF =new ShapefileWorkspaceFactory();
        IFeatureWorkspace xjFWs = (IFeatureWorkspace)xjWsF.openFromFile(xjFolder, 0);

        //字段
        IFields xjFields = new Fields();
        IFieldsEdit xjFieldsEdit;
        xjFieldsEdit = (IFieldsEdit)xjFields;

        IField xjField = new Field();
        IFieldEdit xjFieldEdit = (IFieldEdit)xjField;
        xjFieldEdit.setName("Shape");
        xjFieldEdit.setType(esriFieldType.esriFieldTypeGeometry);


        IGeometryDef xjGeometryDef = new GeometryDef();
        IGeometryDefEdit xjGDefEdit = (IGeometryDefEdit)xjGeometryDef;
        xjGDefEdit.setGeometryType(esriGeometryType.esriGeometryPolyline);

        //定义坐标系
        ISpatialReferenceFactory pSRF = new SpatialReferenceEnvironment();
        ISpatialReference pSpatialReference = pSRF.createGeographicCoordinateSystem((int)esriSRGeoCSType.esriSRGeoCS_WGS1984);
        xjGDefEdit.setSpatialReferenceByRef(pSpatialReference);

        xjFieldEdit.setGeometryDefByRef(xjGeometryDef);
        xjFieldsEdit.addField(xjField);

        IField xjField2 = new Field();
        IFieldEdit xjFieldEdit2 = (IFieldEdit)xjField2;
        xjFieldEdit2.setName("time");
        xjFieldEdit2.setType(esriFieldType.esriFieldTypeString);
        xjFieldsEdit.addField(xjField2);

        //要素集
        IFeatureClass xjFeatureClass;
        xjFeatureClass = xjFWs.createFeatureClass(xjShapeName, xjFields, null, null,
                esriFeatureType.esriFTSimple, "Shape", "");

        //数据
        IPoint xjPoint = new Point();
        Polyline line2=new Polyline();


        ArrayList<String> files= sdad.getFiles(xlsxFolder);
        for (int i = 0; i < files.size() ; i++) {
            XSSFWorkbook xssfWorkbook=new XSSFWorkbook(new FileInputStream(files.get(i)));
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            XSSFRow titleRow=sheet.getRow(0);
            for(int j=1;j< sheet.getPhysicalNumberOfRows();j++){
                XSSFRow row=sheet.getRow(j);
                Double weidu=0.0;
                Double jingdu=0.0;
                for (int index=0;index<row.getPhysicalNumberOfCells();index++ ){
                    XSSFCell cell=row.getCell(index);
                    XSSFCell titleCell=titleRow.getCell(index);
                    cell.setCellType(CellType.STRING);
                    String titleName=titleCell.getStringCellValue();
                    if(titleName.equals("维度")){
                        weidu = Double.parseDouble(cell.getStringCellValue());
                    }
                    else if(titleName.equals("经度")){
                        jingdu = Double.parseDouble(cell.getStringCellValue());
                    }
                }
                xjPoint.setY(weidu);
                xjPoint.setX(jingdu);
                line2.addPoint(xjPoint,null,null);
            }
            IFeature xjFeature = xjFeatureClass.createFeature();
            xjFeature.setShapeByRef(line2);
            xjFeature.store();

            IFeatureCursor pCursor=xjFeatureClass.IFeatureClass_update(null,false);
            IFeature pFeature=pCursor.nextFeature();
            int a=0;
            int max = xjFeatureClass.featureCount(null);
            while(pFeature !=null)
            {
                if(a==max-1) {
                    pFeature.setValue(xjFeatureClass.findField("time"),new File(files.get(i).trim()).getName());//填写的值：object类型，设置为与字段接受的数据兼容就OK
                    pCursor.updateFeature(pFeature);//将更新的内容保存
                }
                a++;
                pFeature=pCursor.nextFeature();
            }
            line2.setEmpty();
        }




//
//
//        for (int j = 0; j < 2; j++)
//        {
//            xjPoint.setX(2-j);
//            xjPoint.setY(3-j);
//            line2.addPoint(xjPoint,null,null);
//        }
//        IFeature xjFeature = xjFeatureClass.createFeature();
//        xjFeature.setShapeByRef(line2);
//        xjFeature.store();
//        line2.setEmpty();
//
//
//        for (int j = 0; j < 2; j++)
//        {
//            xjPoint.setX(2.1-j);
//            xjPoint.setY(3.1-j);
//            line2.addPoint(xjPoint,null,null);
//        }
//        xjFeature = xjFeatureClass.createFeature();
//        xjFeature.setShapeByRef(line2);
//        xjFeature.store();


    }


    public static ArrayList<String> getFiles(String path) {
        ArrayList<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
//              System.out.println("文     件：" + tempList[i]);
                files.add(tempList[i].toString());
            }
            if (tempList[i].isDirectory()) {
//              System.out.println("文件夹：" + tempList[i]);
            }
        }
        return files;
    }

}
