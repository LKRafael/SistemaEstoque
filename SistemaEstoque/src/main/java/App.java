import javax.swing.SwingUtilities;
import view.ProdutoView;

public class App {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            ProdutoView tela = new ProdutoView();
            tela.setVisible(true);
        });
    }
}
