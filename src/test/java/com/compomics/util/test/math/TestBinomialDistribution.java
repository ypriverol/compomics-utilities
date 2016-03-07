package com.compomics.util.test.math;

import com.compomics.util.math.statistics.distributions.BinomialDistribution;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.math.util.MathUtils;

/**
 * Test for the probability calculation of the binomial distribution.
 *
 * @author Marc Vaudel
 */
public class TestBinomialDistribution extends TestCase {

    /**
     * Tests the exp function
     */
    public void testP() {
        double tolerance = Math.pow(10, -10);
        int n = 487;
        double p = 0.125;
        BinomialDistribution binomialDistribution = new BinomialDistribution(n, p);
        for (int k = 1; k < n; k++) {
            double result = results[k - 1];
            Double probability = binomialDistribution.getProbabilityAt((double) k);
            Double error = probability - result;
            if (Math.abs(error) >= tolerance) {
                probability = binomialDistribution.getProbabilityAt((double) k);
                System.out.println("Probability: " + probability);
                System.out.println("Estimation: " + error);
                System.out.println("Error: " + error);
            }
            Assert.assertTrue(Math.abs(error) < tolerance);
        }
    }

    /**
     * Results obtained using excel
     */
    private double[] results = {3.98430652880611000000E-27,
        1.38312355214268000000E-25,
        3.19435677518669000000E-24,
        5.52167385425120000000E-23,
        7.61990991886674000000E-22,
        8.74475376403280000000E-21,
        8.58413583775461000000E-20,
        7.35783071807536000000E-19,
        5.59428716501295000000E-18,
        3.82009894982304000000E-17,
        2.36647688190338000000E-16,
        1.34100356641192000000E-15,
        6.99974389061170000000E-15,
        3.38559041239789000000E-14,
        1.52512787148971000000E-13,
        6.42732460127806000000E-13,
        2.54392427495966000000E-12,
        9.48924134310343000000E-12,
        3.34620615783122000000E-11,
        1.11858891561787000000E-10,
        3.55361240539825000000E-10,
        1.07531388371141000000E-09,
        3.10572022314168000000E-09,
        8.57770347343886000000E-09,
        2.26941526182984000000E-08,
        5.76082335695263000000E-08,
        1.40515321034666000000E-07,
        3.29780855489523000000E-07,
        7.45662131377782000000E-07,
        1.62625360081440000000E-06,
        3.42487509480268000000E-06,
        6.97206715727686000000E-06,
        1.37328595522120000000E-05,
        2.61962951122029000000E-05,
        4.84364150441955000000E-05,
        8.68780142856204000000E-05,
        1.51281793215501000000E-04,
        2.55927845665320000000E-04,
        4.20921621625385000000E-04,
        6.73474594600614000000E-04,
        1.04893081458701000000E-03,
        1.59123518131228000000E-03,
        2.35249055044506000000E-03,
        3.39125261168054000000E-03,
        4.76928541896659000000E-03,
        6.54665886702867000000E-03,
        8.77530869410226000000E-03,
        1.14914756708482000000E-02,
        1.47077487449049000000E-02,
        1.84056970007667000000E-02,
        2.25302229393138000000E-02,
        2.69867505536836000000E-02,
        3.16421468756128000000E-02,
        3.63298723386665000000E-02,
        4.08593109679029000000E-02,
        4.50286284136073000000E-02,
        4.86399469831196000000E-02,
        5.15152147850775000000E-02,
        5.35109616048383000000E-02,
        5.45302180163591000000E-02,
        5.45302180163591000000E-02,
        5.35250527073017000000E-02,
        5.15831006816400000000E-02,
        4.88197202879807000000E-02,
        4.53862454545403000000E-02,
        4.14567003935412000000E-02,
        3.72137971549698000000E-02,
        3.28357033720322000000E-02,
        2.84848027181810000000E-02,
        2.42992806861219000000E-02,
        2.03879276581746000000E-02,
        1.68281307654775000000E-02,
        1.36666815414348000000E-02,
        1.09227918111081000000E-02,
        8.59259622473835000000E-03,
        6.65441662517334000000E-03,
        5.07414699990026000000E-03,
        3.81025690468699000000E-03,
        2.81807427489510000000E-03,
        2.05316840028071000000E-03,
        1.47379107392283000000E-03,
        1.04243758887226000000E-03,
        7.26656150590808000000E-04,
        4.99267151086206000000E-04,
        3.38159095609648000000E-04,
        2.25813881121391000000E-04,
        1.48688614662855000000E-04,
        9.65510484823724000000E-05,
        6.18360647583736000000E-05,
        3.90646885298930000000E-05,
        2.43464385343289000000E-05,
        1.49707913968855000000E-05,
        9.08365991055261000000E-06,
        5.43915198291452000000E-06,
        3.21441613426373000000E-06,
        1.87507607832054000000E-06,
        1.07975662242022000000E-06,
        6.13855805749102000000E-07,
        3.44574182447915000000E-07,
        1.90992546842556000000E-07,
        1.04546132430084000000E-07,
        5.65193377002974000000E-08,
        3.01802288690907000000E-08,
        1.59192416012786000000E-08,
        8.29533269835344000000E-09,
        4.27064297947567000000E-09,
        2.17238314443290000000E-09,
        1.09193861757209000000E-09,
        5.42391528256654000000E-10,
        2.66264932053265000000E-10,
        1.29191607958920000000E-10,
        6.19592405517274000000E-11,
        2.93738498190870000000E-11,
        1.37666915192210000000E-11,
        6.37885209524152000000E-12,
        2.92233125545553000000E-12,
        1.32379108153112000000E-12,
        5.92981477199169000000E-13,
        2.62677269011396000000E-13,
        1.15077660709755000000E-13,
        4.98624574740023000000E-14,
        2.13696246317157000000E-14,
        9.05913239323588000000E-15,
        3.79899100361508000000E-15,
        1.57603855349976000000E-15,
        6.46854825812813000000E-16,
        2.62671082247955000000E-16,
        1.05537488403195000000E-16,
        4.19578719122338000000E-17,
        1.65065034555816000000E-17,
        6.42619600179152000000E-18,
        2.47589369765984000000E-18,
        9.44084063017473000000E-19,
        3.56296117599334000000E-19,
        1.33092623822822000000E-19,
        4.92107180521366000000E-20,
        1.80114306947863000000E-20,
        6.52588068651683000000E-21,
        2.34073212702398000000E-21,
        8.31198755310555000000E-22,
        2.92224891684663000000E-22,
        1.01720133322831000000E-22,
        3.50583876087676000000E-23,
        1.19643703744206000000E-23,
        4.04313205756285000000E-24,
        1.35298548305920000000E-24,
        4.48365451626044000000E-25,
        1.47146962888853000000E-25,
        4.78262899514120000000E-26,
        1.53955104795968000000E-26,
        4.90850239510321000000E-27,
        1.55005338792736000000E-27,
        4.84843963544042000000E-28,
        1.50220671450559000000E-28,
        4.61045931733068000000E-29,
        1.40171473750347000000E-29,
        4.22172500558374000000E-30,
        1.25964670148520000000E-30,
        3.72348396036500000000E-31,
        1.09044887410691000000E-31,
        3.16394660011493000000E-32,
        9.09564895623871000000E-33,
        2.59078519787687000000E-33,
        7.31197216125553000000E-34,
        2.04481992042029000000E-34,
        5.66636845417685000000E-35,
        1.55594890828983000000E-35,
        4.23387458038043000000E-36,
        1.14167877526742000000E-36,
        3.05087269357175000000E-37,
        8.07958766802209000000E-38,
        2.12055623180645000000E-38,
        5.51589771279132000000E-39,
        1.42199661889692000000E-39,
        3.63334646297758000000E-40,
        9.20133195169646000000E-41,
        2.30961601047417000000E-41,
        5.74623566008845000000E-42,
        1.41706849079591000000E-42,
        3.46394519972339000000E-43,
        8.39330052340199000000E-44,
        2.01597328113117000000E-44,
        4.79993638364554000000E-45,
        1.13290423961824000000E-45,
        2.65073347184813000000E-46,
        6.14839868278125000000E-47,
        1.41380290566626000000E-47,
        3.22295495212675000000E-48,
        7.28392691372568000000E-49,
        1.63203775961674000000E-49,
        3.62539427528920000000E-50,
        7.98449929676788000000E-51,
        1.74346949855409000000E-51,
        3.77452159480775000000E-52,
        8.10208664673026000000E-53,
        1.72435080236537000000E-53,
        3.63876782805154000000E-54,
        7.61358347860745000000E-55,
        1.57955895571962000000E-55,
        3.24937842319463000000E-56,
        6.62808534084506000000E-57,
        1.34061697841699000000E-57,
        2.68878141343308000000E-58,
        5.34743642447463000000E-59,
        1.05458153876405000000E-59,
        2.06235779425428000000E-60,
        3.99946542571028000000E-61,
        7.69127966482745000000E-62,
        1.46675804954681000000E-62,
        2.77386896444897000000E-63,
        5.20217808498557000000E-64,
        9.67520991547181000000E-65,
        1.78449545724663000000E-65,
        3.26403040911599000000E-66,
        5.92079934676863000000E-67,
        1.06511734280488000000E-67,
        1.90024226399030000000E-68,
        3.36215865843627000000E-69,
        5.89967827214173000000E-70,
        1.02669725774941000000E-70,
        1.77199849915378000000E-71,
        3.03315058413714000000E-72,
        5.14916659062344000000E-73,
        8.66951517809052000000E-74,
        1.44767142338916000000E-74,
        2.39753421572659000000E-75,
        3.93805179549801000000E-76,
        6.41537259918223000000E-77,
        1.03654491777177000000E-77,
        1.66104713531140000000E-78,
        2.64000688790988000000E-79,
        4.16158721246864000000E-80,
        6.50646682513492000000E-81,
        1.00893930011251000000E-81,
        1.55174251020344000000E-82,
        2.36706484607306000000E-83,
        3.58127351636131000000E-84,
        5.37405989850093000000E-85,
        7.99845137314283000000E-86,
        1.18072377413066000000E-86,
        1.72874198109228000000E-87,
        2.51045175530507000000E-88,
        3.61587701381420000000E-89,
        5.16553859116280000000E-90,
        7.31910132742046000000E-91,
        1.02858450710555000000E-91,
        1.43371235519047000000E-92,
        1.98209081362736000000E-93,
        2.71784110417075000000E-94,
        3.69626390167203000000E-95,
        4.98585398233523000000E-96,
        6.67041689246628000000E-97,
        8.85120253941066000000E-98,
        1.16489392250958000000E-98,
        1.52056181481644000000E-99,
        1.96858449239631000000E-100,
        2.52775440657889000000E-101,
        3.21917781568741000000E-102,
        4.06614296631220000000E-103,
        5.09384943032506000000E-104,
        6.32897548267038000000E-105,
        7.79906466239635000000E-106,
        9.53171944073414000000E-107,
        1.15535993221024000000E-107,
        1.38892326082415000000E-108,
        1.65596650860880000000E-109,
        1.95809843982116000000E-110,
        2.29627748806293000000E-111,
        2.67065730157076000000E-112,
        3.08044069705002000000E-113,
        3.52375135086902000000E-114,
        3.99753304510331000000E-115,
        4.49748615749480000000E-116,
        5.01805024871704000000E-117,
        5.55244001546348000000E-118,
        6.09273956148155000000E-119,
        6.63005697510336000000E-120,
        7.15473774291763000000E-121,
        7.65663178837603000000E-122,
        8.12540516317398000000E-123,
        8.55088392870910000000E-124,
        8.92341483948330000000E-125,
        9.23422535130816000000E-126,
        9.47576444500441000000E-127,
        9.64200592649572000000E-128,
        9.72869728847165000000E-129,
        9.73353984560898000000E-130,
        9.65628952937431000000E-131,
        9.49877220141114000000E-132,
        9.26481229497201000000E-133,
        8.96007865542272000000E-134,
        8.59185624492581000000E-135,
        8.16875654685838000000E-136,
        7.70038275068219000000E-137,
        7.19696789773204000000E-138,
        6.66900500175912000000E-139,
        6.12688771205441000000E-140,
        5.58057845297383000000E-141,
        5.03931833546104000000E-142,
        4.51138974793651000000E-143,
        4.00393869418171000000E-144,
        3.52285996744485000000E-145,
        3.07274443176466000000E-146,
        2.65688428310468000000E-147,
        2.27732938551837000000E-148,
        1.93498575240109000000E-149,
        1.62974602691778000000E-150,
        1.36064139538581000000E-151,
        1.12600466839597000000E-152,
        9.23635165781142000000E-154,
        7.50957392481673000000E-155,
        6.05167129472401000000E-156,
        4.83360327054626000000E-157,
        3.82641933155184000000E-158,
        3.00213398802014000000E-159,
        2.33438990026893000000E-160,
        1.79892146438005000000E-161,
        1.37383939328232000000E-162,
        1.03976201282896000000E-163,
        7.79821509621783000000E-165,
        5.79573618632973000000E-166,
        4.26837713811360000000E-167,
        3.11491476244396000000E-168,
        2.25240750018029000000E-169,
        1.61381284628306000000E-170,
        1.14565153855324000000E-171,
        8.05809950664371000000E-173,
        5.61540035306165000000E-174,
        3.87689386077648000000E-175,
        2.65172826840989000000E-176,
        1.79681199024750000000E-177,
        1.20612164577719000000E-178,
        8.02011390370942000000E-180,
        5.28270975693431000000E-181,
        3.44671468149666000000E-182,
        2.22746867171523000000E-183,
        1.42580656816035000000E-184,
        9.03934848791390000000E-186,
        5.67578139359132000000E-187,
        3.52947750525828000000E-188,
        2.17357852229993000000E-189,
        1.32557420324046000000E-190,
        8.00534191877958000000E-192,
        4.78724765907025000000E-193,
        2.83468494926349000000E-194,
        1.66195401649612000000E-195,
        9.64740701218463000000E-197,
        5.54448678861153000000E-198,
        3.15466092352448000000E-199,
        1.77691105080162000000E-200,
        9.90788823605296000000E-202,
        5.46863961080809000000E-203,
        2.98772297636254000000E-204,
        1.61563712200379000000E-205,
        8.64707192058431000000E-207,
        4.58031096917008000000E-208,
        2.40104336519096000000E-209,
        1.24555322216615000000E-210,
        6.39380683085665000000E-212,
        3.24764791408617000000E-213,
        1.63217762203754000000E-214,
        8.11580033057342000000E-216,
        3.99242440504402000000E-217,
        1.94293809350659000000E-218,
        9.35347888459185000000E-220,
        4.45403756409102000000E-221,
        2.09785342645020000000E-222,
        9.77260912942593000000E-224,
        4.50228604878740000000E-225,
        2.05123457048998000000E-226,
        9.24121851164066000000E-228,
        4.11667184082312000000E-229,
        1.81316454115160000000E-230,
        7.89536889577097000000E-232,
        3.39876832465552000000E-233,
        1.44628439347023000000E-234,
        6.08327274252464000000E-236,
        2.52894936386113000000E-237,
        1.03903309710087000000E-238,
        4.21863061980799000000E-240,
        1.69251397195158000000E-241,
        6.70929248417601000000E-243,
        2.62766024184411000000E-244,
        1.01665426023745000000E-245,
        3.88554318383865000000E-247,
        1.46678536177500000000E-248,
        5.46863497745513000000E-250,
        2.01348857785533000000E-251,
        7.32043221475050000000E-253,
        2.62784746170538000000E-254,
        9.31316053289961000000E-256,
        3.25824858293787000000E-257,
        1.12516763133087000000E-258,
        3.83487154985879000000E-260,
        1.28984829705905000000E-261,
        4.28088179399175000000E-263,
        1.40180008367471000000E-264,
        4.52842812385989000000E-266,
        1.44300072690138000000E-267,
        4.53514514168978000000E-269,
        1.40562033247943000000E-270,
        4.29578353209808000000E-272,
        1.29436937337224000000E-273,
        3.84466150506664000000E-275,
        1.12559754822048000000E-276,
        3.24767765496370000000E-278,
        9.23348157431021000000E-280,
        2.58640940456874000000E-281,
        7.13679158089099000000E-283,
        1.93961583034685000000E-284,
        5.19118592063571000000E-286,
        1.36799628976538000000E-287,
        3.54893537642358000000E-289,
        9.06215382523718000000E-291,
        2.27723658947403000000E-292,
        5.63053002892014000000E-294,
        1.36953625232376000000E-295,
        3.27640251752119000000E-297,
        7.70786817964373000000E-299,
        1.78277223202639000000E-300,
        4.05312994726043000000E-302,
        9.05574057275576000000E-304,
        1.98792008520460000000E-305,
        4.28662012982102000000E-307,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00,
        0.00000000000000000000E+00};
}
