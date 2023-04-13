import com.esri.arcgis.datasourcesfile.ShapefileWorkspaceFactory;
import com.esri.arcgis.geodatabase.*;
import com.esri.arcgis.geometry.*;
import com.esri.arcgis.system.AoInitialize;
import com.esri.arcgis.system.EngineInitializer;
import com.esri.arcgis.system.esriLicenseProductCode;
import com.esri.arcgis.system.esriLicenseStatus;

import javax.swing.*;
import java.io.IOException;

public class createLineShp {
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

    public void createPolygon() throws IOException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        EngineInitializer.initializeVisualBeans();
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        initializeArcGISLicenses();


        String xjFolder = "D:\\baidu";
        String xjShapeName = "test.shp";
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
        ISpatialReference pSpatialReference = pSRF.createGeographicCoordinateSystem((int) esriSRGeoCSType.esriSRGeoCS_Beijing1954);
        xjGDefEdit.setSpatialReferenceByRef(pSpatialReference);

        xjFieldEdit.setGeometryDefByRef(xjGeometryDef);
        xjFieldsEdit.addField(xjField);

        //要素集
        IFeatureClass xjFeatureClass;
        xjFeatureClass = xjFWs.createFeatureClass(xjShapeName, xjFields, null, null,
                esriFeatureType.esriFTSimple, "Shape", "");

        //数据
        IPoint xjPoint = new Point();

        Polyline line2=new Polyline();

        for (int j = 0; j < 2; j++)
        {
            xjPoint.setX(2.2-j);
            xjPoint.setY(3-j);

            line2.addPoint(xjPoint,null,null);


        }
        IFeature xjFeature = xjFeatureClass.createFeature();
        xjFeature.setShapeByRef(line2);
        xjFeature.store();



    }


    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, IOException {
        createLineShp sa=new createLineShp();
        sa.createPolygon();
    }
}
