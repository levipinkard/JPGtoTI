import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.io.*;
class Main {
  static BufferedImage imageIn;
  static PrintWriter writer;
  public static void main(String[] args) {
  Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
  String contents = "";
  String s = null;
    try {
	  ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd \"C:\\Users\\levip\\Downloads\" && convert.exe " + args[0] + " -resize " + args[1] + "x" + args[2] + " -threshold " + args[3] + "% " + "copy"+args[0]);
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) { break; }
            System.out.println(line);
        }
		System.out.println("Success?");
	}catch(Exception e) {
      System.out.println(e.getMessage());
    }
    try {
        imageIn = ImageIO.read(new File("copy"+args[0]));
    } catch (IOException e) {System.out.println(e.getMessage());}
    int blackVal = 0;
	System.out.println(imageIn.getWidth());
	contents += "()\nPrgm\nClrDraw\n";
	//writer.println("()");
	//writer.println("Prgm");
	//writer.println("ClrDraw");
	int blackCount = 0;
	for (int x = 0; x < imageIn.getWidth()-1;x++) {
      for (int y = 0; y < imageIn.getHeight()-1; y++) {
        blackVal = new Color(imageIn.getRGB(x,y)).getGreen();
        if (!(blackVal > 0 )) {
			blackCount++;
          //contents += "\nPxlOn(" + y + "),(" + x + ")";
        } //else writer.println("Pxl-Off(" + y+ "),(" + x + ")");
		//writer.flush();
      }
    }
	System.out.println(blackCount);
	boolean white = false;
	if ((double)blackCount/(imageIn.getWidth() * imageIn.getHeight()) > .5) white = true;
	if (white) {
			contents += "\nFor u,0," + (imageIn.getWidth()-1) + "\nFor i,0," + (imageIn.getHeight()-1) + "\nPxlOn(i),(u)\nEndFor\nEndFor";
			System.out.println("WHITE");
	}
    for (int x = 0; x < imageIn.getWidth()-1;x++) {
      for (int y = 0; y < imageIn.getHeight()-1; y++) {
        blackVal = new Color(imageIn.getRGB(x,y)).getGreen();
        if (white) {
			if (!(blackVal == 0 )) {
			  contents += "\nPxlOff(" + y + "),(" + x + ")";
			}
		} else {
			if (!(blackVal > 0 )) {
			  contents += "\nPxlOn(" + y + "),(" + x + ")";
			} //else writer.println("Pxl-Off(" + y+ "),(" + x + ")");
			//writer.flush();
		}
      }
    }
    contents += "\nEndPrgm";
	Transferable transferable = new StringSelection(contents);
	clipboard.setContents(transferable,null);
	//writer.println("EndPrgm");
    //writer.flush();
	//writer.close();
  }
}