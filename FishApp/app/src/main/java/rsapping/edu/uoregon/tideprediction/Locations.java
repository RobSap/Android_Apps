package rsapping.edu.uoregon.tideprediction;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by joey on 9/24/2016.
 */

public class Locations {
    ////////////
    // Oregon //
    ////////////
    protected static final Map<String, String> Oregon;
    static {
/*
        West Coast
        Alaska
                California

        Washington
                Oregon

        East Coast
        Maine
        New Hampshire
        Massachusetts
        Rhode Island
        Connecticut
        New York
        New Jersey
        Delaware
                Pennsylvania
        Maryland
                Virginia
        Washington DC
        North Carolina
        South Carolina
        Georgia
                Florida
        Gulf Coast
        Alabama
                Mississippi
        Louisiana
                Texas
        Pacific
        Northern Marianas Islands
        Federated States of Micronesia
        Marshall Islands
        Hawaii
        French Polynesia
        Cook Islands
        Fiji
                Tokelau
        American Samoa
        Kiribati
                */
        Oregon = new HashMap<String, String>();

        //Meteorological & Ancillary Stations Via CO-OPS/SOS
        Oregon.put("Port Orford", "9431647"); //9431647  	Port Orford, OR
        Oregon.put("Charleston", "9432780");
        Oregon.put("South Beach", "9435380");  //9435380  	South Beach, OR
        Oregon.put("Garibaldi", "9437540"); // 9437540  	Garibaldi, OR
        Oregon.put("Astoria", "9439040");

        Oregon.put("Wauna", "9439099");
        Oregon.put("Helens, OR", "9439201");

       // Umpqua River
        Oregon.put("Umpqua River Entrance, Half Moon Bay","9433445");//  Harmonic
        //Oregon.put("Gardiner","TWC0831");//  Subordinate
        Oregon.put("Reedsport","9433501");// 	 Subordinate

       // Siuslaw River
        Oregon.put("Suislaw River Entrance","9434132");// 	Subordinate
        Oregon.put("Florence USCG Pier","9434098"); 		//Harmonic
        Oregon.put("Florence","9434032"); 	//Subordinate

        //Coos Bay
       // Oregon.put("CHARLESTON","9432780");// 		Harmonic
        Oregon.put("Sitka Dock, Coos Bay","9432879");// 	Subordinate
        Oregon.put("Empire","9432864");// 		Subordinate
        Oregon.put("Coos Bay","9432845");//		Subordinate

        //Yaquina Bay and River
        Oregon.put("YAQUINA USCG STA, NEWPORT","9435385");//	Subordinate
        Oregon.put("Southbeach","9435380");//	Harmonic
        Oregon.put("Weiser Point, Yaquina River","9435308");//	Harmonic
        Oregon.put("TOLEDO","9435362");//	Subordinate

        //Nehalem River
        Oregon.put("Brighton","9437815");//	Subordinate
        Oregon.put("Nehalem","9437908");//	Subordinate

        //Tillamook Bay
        Oregon.put("Barview","9437581");//	Subordinate
        //Oregon.put("Garibaldi","9437540");// Harmonic
       // Oregon.put("Miami Cove","TWC0863");//		Subordinate
       // Oregon.put("Bay City","TWC0865");		//Subordinate
        Oregon.put("Dick Point","9437381");//		Harmonic
        Oregon.put("Tillamook,Hoquarten Slough","	9437331"); //Subordinate

        //Other
        Oregon.put("Brookings, Chetco Cove","9430104");		//Subordinate
        Oregon.put("Gold Beach, Rogue River","9431011");		//Harmonic
        //Oregon.put(" Wedderburn, Rogue River","TWC0817");	//Subordinate
        //Oregon.put("Port Orford","9431647");//	Harmonic
        Oregon.put("Bandon, Coquille River","9432373");		//Subordinate
        Oregon.put("Drift Creek, Alsea River","9434938");		//Subordinate
        Oregon.put("Waldport, Alsea Bay","9434939");		//Harmonic
        Oregon.put("Depoe Bay","9435827");		//Harmonic
        Oregon.put("Taft, Siletz Bay","9436101");	//Subordinate
        Oregon.put("Kernville, Siletz River","9436031");		//Subordinate
        Oregon.put("Cascade Head, Salmon River","9436381");		//Harmonic
        Oregon.put("Netarts, Netarts Bay","9437262");		//Subordinate
       // Oregon.put("Nestucca Bay entrance","TWC0857");		//Subordinate

    }
}