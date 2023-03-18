package darkchoco.dp.proxy;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class Printer implements Printable {

    private String printerName;

    public Printer(String printerName) {
        this.printerName = printerName;
        heavyJob("The instance of Printer(" + printerName +") is being created");
    }

    @Override
    public void print(String str) {
        System.out.println("=== " + printerName + " ===");
        System.out.println(str);
    }

    private void heavyJob(String msg) {
        System.out.println(msg);

        for (int i=0; i < 5; i++) {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(".");
        }
        System.out.println("Done.");
    }
}
