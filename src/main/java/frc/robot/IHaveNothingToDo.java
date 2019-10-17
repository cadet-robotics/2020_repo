package frc.robot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import edu.wpi.first.wpilibj.Filesystem;

/**
 * I have nothing to do so im making this
 * 
 * @author Alex Pickering
 */
public class IHaveNothingToDo {
	/**
	 *      ::        .+shmmhhhhys/.      .-.            `/shdmdhhhhy+-      
	 *    `hd.      /hsdm/:om:   ./yh-    sMo          .yysNo:/do   .dMd+    
	 *   `dm`      `:` do  `ds      .:`   sMo          :. od`  +m    `dm+.   
	 *   dM-           .yhyho`            sNmdy-          `ohyhy-     -Md
	 *  /Mh                                  -dMo                      yM+
	 *  hM+                                   .NM.                     +Mh
	 *  dM/                                   `NM-                     /Md
	 *  sMs                           .+/     :Mm`                     oMy
	 *  -Mm`                          `hMy/:/sNm-                      mM-
	 *   oMo                        --  -+sss+-      --               +Mo
	 *    oM/                       .yy/.         .+hs`              :Ms
	 *     /m/                        `/syhhyyyhhys:`               :m/   
	 */
	public static void yeet() {
		String p = Filesystem.getDeployDirectory().getPath() + "/logo.txt";
		System.out.println(p);
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(p));
		} catch(FileNotFoundException e1) {
			System.out.println("fnf");
			return;
		}
		String s = "";
		
		try {
			while((s = br.readLine()) != null) {
				System.out.println(s);
			}
		} catch(IOException e) {
			System.out.println(":(");
		} finally {
			try {
				br.close();
			} catch(IOException e) {
				System.out.println("what");
			}
		}
	}
}
