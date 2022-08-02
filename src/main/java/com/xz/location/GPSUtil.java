package com.xz.location;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class GPSUtil {
    public static double pi = 3.1415926535897932384626;
    public static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
    public static double a = 6378245.0;
    public static double ee = 0.00669342162296594323;

    public static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
                * pi)) * 2.0 / 3.0;
        return ret;
    }

    public static double[] transform(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new double[]{lat, lon};
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new double[]{mgLat, mgLon};
    }

    public static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    /**
     * 84 to 火星坐标系 (GCJ-02) World Geodetic System ==> Mars Geodetic System
     *
     * @param lat
     * @param lon
     * @return
     */
    public static double[] gps84_To_Gcj02(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new double[]{lat, lon};
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new double[]{mgLat, mgLon};
    }

    /**
     * * 火星坐标系 (GCJ-02) to 84 * * @param lon * @param lat * @return
     */
    public static double[] gcj02_To_Gps84(double lat, double lon) {
        double[] gps = transform(lat, lon);
        double lontitude = lon * 2 - gps[1];
        double latitude = lat * 2 - gps[0];
        return new double[]{latitude, lontitude};
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
     *
     * @param lat
     * @param lon
     */
    public static double[] gcj02_To_Bd09(double lat, double lon) {
        double x = lon, y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double tempLon = z * Math.cos(theta) + 0.0065;
        double tempLat = z * Math.sin(theta) + 0.006;
        double[] gps = {tempLat, tempLon};
        return gps;
    }

    /**
     * * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 * * 将 BD-09 坐标转换成GCJ-02 坐标 * * @param
     * bd_lat * @param bd_lon * @return
     */
    public static double[] bd09_To_Gcj02(double lat, double lon) {
        double x = lon - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double tempLon = z * Math.cos(theta);
        double tempLat = z * Math.sin(theta);
        double[] gps = {tempLat, tempLon};
        return gps;
    }

    /**
     * 将gps84转为bd09
     *
     * @param lat
     * @param lon
     * @return
     */
    public static double[] gps84_To_bd09(double lat, double lon) {
        double[] gcj02 = gps84_To_Gcj02(lat, lon);
        double[] bd09 = gcj02_To_Bd09(gcj02[0], gcj02[1]);
        return bd09;
    }

    public static double[] bd09_To_gps84(double lat, double lon) {
        double[] gcj02 = bd09_To_Gcj02(lat, lon);
        double[] gps84 = gcj02_To_Gps84(gcj02[0], gcj02[1]);
        //保留小数点后六位
        gps84[0] = retain6(gps84[0]);
        gps84[1] = retain6(gps84[1]);
        return gps84;
    }

    /**
     * 保留小数点后六位
     *
     * @param num
     * @return
     */
    private static double retain6(double num) {
        String result = String.format("%.6f", num);
        return Double.valueOf(result);
    }

    public static void main(String[] args) {
        String dist = "113.308242,22.95771],[113.308328,22.957275],[113.308371,22.956525],[113.308285,22.956209],[113.3085,22.955261],[113.3085,22.954312],[113.308243,22.953839],[113.307429,22.953841],[113.307215,22.954276],[113.307258,22.955223],[113.307001,22.955382],[113.306615,22.955066],[113.306315,22.954316],[113.306272,22.953646],[113.30563,22.953844],[113.30533,22.954397],[113.304859,22.955267],[113.30456,22.955149],[113.304087,22.954676],[113.304088,22.954162],[113.304645,22.953727],[113.304602,22.953174],[113.304302,22.952819],[113.304259,22.952069],[113.304003,22.951397],[113.303574,22.950963],[113.302889,22.951478],[113.302504,22.951242],[113.302203,22.95069],[113.302074,22.950136],[113.301947,22.94911],[113.301754,22.948628],[113.301646,22.94836],[113.301132,22.947887],[113.300747,22.947216],[113.299719,22.946861],[113.298991,22.946863],[113.298819,22.946863],[113.298477,22.946863],[113.297963,22.947378],[113.297363,22.94809],[113.29672,22.948683],[113.296206,22.948921],[113.295449,22.94917],[113.295771,22.949091],[113.296092,22.949268],[113.296434,22.949583],[113.296692,22.950077],[113.29712,22.950293],[113.297634,22.950529],[113.298148,22.950706],[113.298705,22.951021],[113.298683,22.951357],[113.298491,22.952148],[113.298362,22.9528],[113.298277,22.95357],[113.298148,22.953906],[113.297976,22.954282],[113.297698,22.954677],[113.297398,22.954895],[113.297077,22.955073],[113.29667,22.955291],[113.296348,22.95543],[113.296477,22.955548],[113.296627,22.955863],[113.296862,22.956081],[113.296884,22.956357],[113.297055,22.956593],[113.297206,22.957029],[113.297098,22.957463],[113.296563,22.957662],[113.296563,22.957919],[113.296991,22.958432],[113.297312,22.959418],[113.297312,22.960051],[113.297098,22.960624],[113.297312,22.9608],[113.29774,22.961096],[113.298126,22.961412],[113.298126,22.961748],[113.298019,22.962183],[113.297847,22.962558],[113.297719,22.962716],[113.297183,22.962717],[113.296605,22.962619],[113.296434,22.962698],[113.296241,22.963153],[113.296155,22.963647],[113.295941,22.964022],[113.295919,22.964457],[113.29579,22.964911],[113.29562,22.965484],[113.295427,22.965938],[113.295276,22.966511],[113.295148,22.967084],[113.294977,22.967303],[113.294526,22.967205],[113.293927,22.967048],[113.293113,22.96697],[113.293221,22.967108],[113.293456,22.967502],[113.293735,22.967897],[113.293862,22.968272],[113.293948,22.968588],[113.294077,22.968786],[113.29412,22.968984],[113.294098,22.969201],[113.294205,22.969516],[113.294376,22.969753],[113.294483,22.970227],[113.294676,22.970661],[113.294827,22.971036],[113.294911,22.971371],[113.295169,22.971786],[113.295426,22.972338],[113.295554,22.972753],[113.295769,22.973128],[113.295576,22.973306],[113.295405,22.973524],[113.295062,22.973702],[113.294333,22.97392],[113.293926,22.974059],[113.293583,22.974197],[113.293133,22.974317],[113.29277,22.974376],[113.292491,22.974476],[113.292191,22.974535],[113.291912,22.974654],[113.29142,22.974714],[113.291077,22.974882],[113.290713,22.975043],[113.290691,22.974944],[113.290455,22.974608],[113.29037,22.974214],[113.290156,22.97376],[113.289771,22.973445],[113.289363,22.973642],[113.288956,22.973821],[113.288613,22.974018],[113.288099,22.974158],[113.287671,22.974395],[113.287574,22.974438],[113.287528,22.974458],[113.287452,22.974492],[113.287177,22.974613],[113.286921,22.974692],[113.28647,22.97491],[113.286171,22.975069],[113.285571,22.975307],[113.285249,22.975544],[113.285399,22.97584],[113.285634,22.976175],[113.285806,22.976412],[113.285956,22.976767],[113.286085,22.977063],[113.286213,22.977478],[113.286213,22.977715],[113.28632,22.97805],[113.286277,22.978465],[113.286341,22.978879],[113.286299,22.979334],[113.285978,22.979709],[113.285527,22.980184],[113.285142,22.980639],[113.284713,22.980994],[113.284263,22.981232],[113.283792,22.981548],[113.283256,22.981925],[113.282956,22.982102],[113.282505,22.9823],[113.282163,22.982459],[113.281734,22.982578],[113.28137,22.982716],[113.281048,22.982776],[113.280556,22.982914],[113.280213,22.982975],[113.279677,22.983192],[113.279226,22.98339],[113.278927,22.983588],[113.278798,22.983924],[113.278584,22.984339],[113.278434,22.984714],[113.27824,22.985029],[113.278133,22.985384],[113.278047,22.985898],[113.277984,22.986272],[113.277961,22.986963],[113.278069,22.987694],[113.278133,22.988089],[113.278283,22.988464],[113.278434,22.988819],[113.278627,22.989254],[113.279012,22.989786],[113.279131,22.990021],[113.279162,22.990083],[113.279334,22.990082],[113.279612,22.989845],[113.279783,22.990101],[113.280041,22.990398],[113.280254,22.990752],[113.280512,22.991088],[113.280662,22.991423],[113.281004,22.991798],[113.281283,22.992192],[113.281519,22.992528],[113.281691,22.992824],[113.281862,22.9931],[113.282141,22.993376],[113.282312,22.993652],[113.282547,22.993987],[113.282826,22.994461],[113.283127,22.994855],[113.283405,22.995231],[113.283555,22.995546],[113.283834,22.995901],[113.284047,22.996414],[113.284241,22.996434],[113.284498,22.996276],[113.284647,22.996374],[113.284948,22.996788],[113.285012,22.997124],[113.285333,22.997123],[113.285376,22.99736],[113.285119,22.99744],[113.285333,22.997834],[113.285612,22.99817],[113.285891,22.998426],[113.285954,22.998781],[113.286214,22.999077],[113.286493,22.999017],[113.28705,22.999036],[113.287435,22.998957],[113.28765,22.99874],[113.2878,22.998503],[113.28795,22.998206],[113.28825,22.997949],[113.288335,22.998205],[113.288271,22.998482],[113.2884,22.998798],[113.288507,22.999114],[113.288678,22.999113],[113.289043,22.999093],[113.289428,22.999033],[113.289857,22.998954],[113.29007,22.998796],[113.290521,22.998716],[113.290799,22.998893],[113.290971,22.998537],[113.291228,22.9983],[113.291635,22.998182],[113.291828,22.998358],[113.29202,22.998615],[113.292043,22.998971],[113.292043,22.999188],[113.292128,22.999325],[113.292599,22.999187],[113.292835,22.999324],[113.293093,22.999364],[113.2929,22.999482],[113.292814,22.999601],[113.292685,22.999798],[113.292472,22.999819],[113.292107,22.999977],[113.292,23.000253],[113.292193,23.000293],[113.292472,23.000293],[113.292514,23.00045],[113.292664,23.00045],[113.292986,23.00045],[113.293178,23.000548],[113.293392,23.000686],[113.293349,23.000963],[113.293242,23.001279],[113.293414,23.001318],[113.293692,23.001317],[113.293885,23.001357],[113.294099,23.001416],[113.294099,23.001554],[113.294142,23.00177],[113.294164,23.002047],[113.29427,23.001988],[113.294334,23.001691],[113.294356,23.001494],[113.294635,23.001454],[113.294721,23.001553],[113.294742,23.001711],[113.294721,23.001909],[113.294678,23.002125],[113.294721,23.002382],[113.294785,23.00248],[113.29517,23.002599],[113.295749,23.002677],[113.296155,23.002597],[113.296391,23.002814],[113.296606,23.00309],[113.296691,23.003228],[113.296906,23.003149],[113.297227,23.003108],[113.29757,23.003108],[113.297998,23.003206],[113.298234,23.003364],[113.298512,23.003363],[113.298748,23.003442],[113.299048,23.003481],[113.299369,23.00354],[113.29969,23.003539],[113.299948,23.003618],[113.300505,23.003617],[113.300826,23.003639],[113.301062,23.003656],[113.301447,23.003674],[113.301704,23.003773],[113.302004,23.003772],[113.302282,23.003772],[113.302685,23.003836],[113.302775,23.00385],[113.303075,23.003908],[113.303311,23.004047],[113.303611,23.004322],[113.303953,23.004362],[113.304382,23.004361],[113.304703,23.004282],[113.304768,23.004123],[113.304725,23.003966],[113.304682,23.003709],[113.304617,23.003274],[113.304746,23.002938],[113.305324,23.002957],[113.30586,23.002956],[113.306524,23.002995],[113.307102,23.002934],[113.30738,23.002973],[113.307659,23.002973],[113.308045,23.002913],[113.308258,23.002853],[113.308387,23.002636],[113.308494,23.002577],[113.308709,23.002438],[113.30918,23.00226],[113.309244,23.002141],[113.309244,23.001845],[113.309308,23.001529],[113.309436,23.001174],[113.309822,23.001173],[113.309822,23.001429],[113.309758,23.001647],[113.310015,23.001823],[113.310186,23.002041],[113.310443,23.002119],[113.310828,23.002257],[113.311128,23.002374],[113.311406,23.002394],[113.311557,23.002294],[113.311664,23.002057],[113.311899,23.002176],[113.312178,23.002096],[113.312434,23.001819],[113.31252,23.001405],[113.312735,23.001089],[113.313164,23.001423],[113.313614,23.001383],[113.313999,23.001224],[113.314085,23.000987],[113.314364,23.000789],[113.314642,23.00065],[113.314941,23.000413],[113.314899,23.00065],[113.314813,23.000946],[113.315027,23.001085],[113.315134,23.001163],[113.315519,23.001143],[113.315926,23.001142],[113.316291,23.001279],[113.31644,23.001496],[113.316783,23.001594],[113.316997,23.001713],[113.317233,23.00181],[113.317382,23.002028],[113.317683,23.002165],[113.318304,23.002144],[113.318625,23.002262],[113.318989,23.002281],[113.319118,23.002162],[113.319096,23.001748],[113.319374,23.001609],[113.319653,23.00153],[113.319931,23.001529],[113.320167,23.00141],[113.320445,23.00133],[113.320445,23.001311],[113.320959,23.00131],[113.321281,23.001329],[113.321731,23.001328],[113.322245,23.001327],[113.322287,23.001505],[113.322437,23.001662],[113.322886,23.001859],[113.323443,23.002035],[113.323679,23.002253],[113.323486,23.00247],[113.323315,23.002746],[113.323486,23.002885],[113.3237,23.002786],[113.323851,23.002884],[113.324065,23.003121],[113.3243,23.003475],[113.32445,23.003752],[113.324857,23.003869],[113.32505,23.003711],[113.325264,23.003473],[113.325436,23.003236],[113.325371,23.002742],[113.325178,23.002348],[113.324857,23.001974],[113.324857,23.001678],[113.325136,23.001657],[113.3255,23.001795],[113.325671,23.001636],[113.325821,23.001301],[113.326227,23.001142],[113.326763,23.001042],[113.326892,23.001259],[113.327341,23.001495],[113.327812,23.001968],[113.32807,23.002106],[113.328176,23.002362],[113.328348,23.002579],[113.328604,23.002717],[113.328797,23.003092],[113.328969,23.003289],[113.329268,23.003289],[113.329568,23.003208],[113.329911,23.00305],[113.329911,23.002932],[113.329911,23.002616],[113.329868,23.002241],[113.329997,23.001826],[113.329954,23.00149],[113.329976,23.001194],[113.330125,23.0007],[113.33004,23.000283],[113.330339,22.999689],[113.330639,22.999097],[113.330382,22.998584],[113.33064,22.998307],[113.331068,22.997872],[113.331111,22.99724],[113.33171,22.996646],[113.331967,22.996291],[113.331967,22.995777],[113.331838,22.995382],[113.331795,22.994987],[113.332438,22.994631],[113.33261,22.994275],[113.332502,22.993904],[113.332438,22.993684],[113.332352,22.993091],[113.332182,22.99242],[113.331838,22.99246],[113.330896,22.992462],[113.330725,22.992305],[113.330425,22.992068],[113.33034,22.991516],[113.330425,22.991239],[113.331154,22.990882],[113.33201,22.990248],[113.332438,22.989852],[113.333038,22.989456],[113.333275,22.989238],[113.333466,22.989061],[113.33379,22.988843],[113.333842,22.988808],[113.333937,22.988744],[113.33458,22.988309],[113.334966,22.988031],[113.33531,22.987756],[113.335608,22.987516],[113.336465,22.987081],[113.33745,22.98625],[113.337279,22.9855],[113.336851,22.985224],[113.336122,22.984712],[113.335694,22.9842],[113.335137,22.983766],[113.334623,22.983174],[113.334238,22.982701],[113.334187,22.982469],[113.334152,22.982306],[113.333982,22.981833],[113.333724,22.981439],[113.333596,22.981083],[113.333296,22.98065],[113.333039,22.980177],[113.33321,22.980059],[113.333981,22.979741],[113.334624,22.979464],[113.334538,22.979108],[113.334538,22.978713],[113.334281,22.978319],[113.334067,22.977963],[113.333768,22.977609],[113.333382,22.977491],[113.332697,22.977334],[113.33214,22.977139],[113.33184,22.977139],[113.331841,22.976546],[113.331841,22.976073],[113.331669,22.975599],[113.331583,22.975401],[113.331497,22.974968],[113.331241,22.974572],[113.33077,22.974653],[113.33077,22.974296],[113.331841,22.97386],[113.332912,22.973622],[113.333981,22.973225],[113.334339,22.972954],[113.331378,22.973754],[113.331045,22.972964],[113.333897,22.971606],[113.333468,22.971093],[113.333125,22.970579],[113.332826,22.970225],[113.332912,22.969909],[113.33334,22.969434],[113.333982,22.969195],[113.33471,22.968878],[113.33471,22.968523],[113.334839,22.967772],[113.334667,22.967417],[113.334753,22.967022],[113.334796,22.966587],[113.334967,22.966271],[113.335053,22.965995],[113.335567,22.965954],[113.335952,22.96619],[113.336424,22.96611],[113.336424,22.965557],[113.336338,22.965163],[113.335952,22.964374],[113.336252,22.96378],[113.336895,22.963226],[113.337794,22.962869],[113.338394,22.96271],[113.338008,22.962079],[113.337452,22.961684],[113.337023,22.961172],[113.336552,22.961094],[113.336081,22.961727],[113.335782,22.961569],[113.335353,22.961688],[113.335222,22.961591],[113.335139,22.961531],[113.334967,22.961215],[113.334539,22.961137],[113.333726,22.961889],[113.332784,22.962444],[113.332184,22.962445],[113.331455,22.962367],[113.330556,22.961777],[113.330214,22.961343],[113.3303,22.960948],[113.331027,22.960433],[113.331541,22.959958],[113.33167,22.959326],[113.331242,22.95905],[113.330857,22.958972],[113.330471,22.958736],[113.330599,22.958064],[113.330599,22.957392],[113.3303,22.956959],[113.329914,22.95684],[113.3291,22.956802],[113.328672,22.956724],[113.328244,22.956646],[113.327687,22.956608],[113.327345,22.956608],[113.326574,22.956452],[113.325803,22.956611],[113.325246,22.95673],[113.324775,22.956731],[113.324818,22.95681],[113.324389,22.956772],[113.323832,22.957247],[113.32349,22.957484],[113.323148,22.957446],[113.32272,22.957447],[113.322376,22.957329],[113.322376,22.957211],[113.322206,22.957092],[113.321948,22.957093],[113.321692,22.957093],[113.321049,22.957055],[113.321006,22.956857],[113.320664,22.956621],[113.320792,22.956305],[113.320236,22.956108],[113.319293,22.956624],[113.31835,22.957099],[113.318137,22.957021],[113.317879,22.956507],[113.317623,22.955876],[113.316766,22.955799],[113.315438,22.956394],[113.31476,22.956822],[113.314496,22.956989],[113.313939,22.957305],[113.312826,22.957583],[113.311926,22.957585],[113.310642,22.957903],[113.309913,22.958102],[113.309399,22.958063],[113.308971,22.957788],[113.308242,22.95771],[113.288336,22.977362],[113.288583,22.977352],[113.288693,22.977356],[113.28884,22.977361],[113.289022,22.977371],[113.289022,22.97747],[113.289027,22.977588],[113.289022,22.977707],[113.289172,22.977702],[113.28936,22.977702],[113.289531,22.977706],[113.289537,22.977898],[113.289526,22.978091],[113.289515,22.978367],[113.28915,22.978378],[113.288819,22.978388],[113.288682,22.978392],[113.288433,22.978399],[113.288208,22.978394],[113.288203,22.977801],[113.288208,22.977367],[113.288336,22.977362],[113.308242,22.95771";
        String[] lngLats = dist.split("],\\[");
        List<String> amap = new ArrayList<>(lngLats.length);
        for (String lngLat : lngLats) {
            /*  System.out.println("lngLat = " + lngLat);
            String s=lngLat.replaceAll("\\[|]","");
            System.out.println("s = " + s);*/
            String[] array = lngLat.split(",");
            //System.out.println(StringUtils.join(array, ","));
            //double[] result = bd09_To_Gcj02(Double.parseDouble(array[1]),Double.parseDouble(array[0]));
            //double[] result = bd09_To_gps84(Double.parseDouble(array[0]),Double.parseDouble(array[1]));
            double[] result = gps84_To_Gcj02(Double.parseDouble(array[1]),Double.parseDouble(array[0]));
            amap.add("[" + result[1] + "," + result[0] + "]");
        }
        System.out.println("StringUtils.join(amap) = " + StringUtils.join(amap, ","));

    }

}