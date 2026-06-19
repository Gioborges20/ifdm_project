package ifdm.view;
import ifdm.controller.IFDMController;
import ifdm.model.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class JanelaPrincipal extends JFrame {
    private final IFDMController controller = new IFDMController();
    private JComboBox<UnidadeFederacao> cmbUF;
    private JComboBox<Cidade> cmbCidade;
    private JTextField txtMediaSaude, txtMediaEducacao, txtMediaRenda, txtMediaIfdm;
    private JTextField txtSaude, txtEducacao, txtRenda, txtIfdm, txtRankNac, txtRankEst;
    private boolean carregando = false;

    private static final Color AZUL  = new Color(25,62,105);
    private static final Color FUNDO = new Color(240,244,250);
    private static final Color BORDA = new Color(180,200,230);

    public JanelaPrincipal() {
        setTitle("Indice FIRJAN de Desenvolvimento Municipal (IFDM 2023)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(820,480); setMinimumSize(new Dimension(720,420));
        setLocationRelativeTo(null);

        JPanel root=new JPanel(new BorderLayout(8,8));
        root.setBorder(new EmptyBorder(12,16,12,16)); root.setBackground(FUNDO);
        root.add(cabecalho(), BorderLayout.NORTH);
        root.add(painelCentral(), BorderLayout.CENTER);
        setContentPane(root);
        carregarEstados();
    }

    private JPanel cabecalho() {
        JPanel p=new JPanel(new BorderLayout()); p.setBackground(AZUL);
        p.setBorder(new EmptyBorder(10,16,10,16));
        JLabel t=new JLabel("IFDM — Indice FIRJAN de Desenvolvimento Municipal");
        t.setFont(new Font("SansSerif",Font.BOLD,16)); t.setForeground(Color.WHITE);
        JLabel s=new JLabel("Ano: 2023  |  Fonte: FIRJAN");
        s.setFont(new Font("SansSerif",Font.PLAIN,11)); s.setForeground(new Color(190,210,240));
        p.add(t,BorderLayout.CENTER); p.add(s,BorderLayout.EAST); return p;
    }

    private JPanel painelCentral() {
        JPanel p=new JPanel(new BorderLayout(8,10)); p.setOpaque(false);
        p.add(barraCombos(), BorderLayout.NORTH);
        p.add(painelDados(), BorderLayout.CENTER);
        return p;
    }

    private JPanel barraCombos() {
        JPanel p=new JPanel(new FlowLayout(FlowLayout.LEFT,12,4)); p.setOpaque(false);
        cmbUF=new JComboBox<>(); cmbUF.setPreferredSize(new Dimension(220,28));
        cmbCidade=new JComboBox<>(); cmbCidade.setPreferredSize(new Dimension(280,28));
        p.add(new JLabel("Estado (UF):")); p.add(cmbUF);
        p.add(Box.createHorizontalStrut(20));
        p.add(new JLabel("Municipio:")); p.add(cmbCidade);
        cmbUF.addActionListener(this::aoSelecionarUF);
        cmbCidade.addActionListener(this::aoSelecionarCidade);
        return p;
    }

    private JPanel painelDados() {
        JPanel p=new JPanel(new GridLayout(1,2,16,0)); p.setOpaque(false);
        txtMediaSaude=campo(); txtMediaEducacao=campo(); txtMediaRenda=campo(); txtMediaIfdm=campo();
        txtSaude=campo(); txtEducacao=campo(); txtRenda=campo(); txtIfdm=campo();
        txtRankNac=campo(); txtRankEst=campo();
        p.add(montarPainel("Medias do Estado",
            new String[]{"Saude:","Educacao:","Emprego & Renda:","IFDM Geral:"},
            new JTextField[]{txtMediaSaude,txtMediaEducacao,txtMediaRenda,txtMediaIfdm}));
        p.add(montarPainel("Dados do Municipio",
            new String[]{"Saude:","Educacao:","Emprego & Renda:","IFDM Geral:","Ranking Nacional:","Ranking Estadual:"},
            new JTextField[]{txtSaude,txtEducacao,txtRenda,txtIfdm,txtRankNac,txtRankEst}));
        return p;
    }

    private JPanel montarPainel(String titulo, String[] rot, JTextField[] campos) {
        JPanel p=new JPanel(new GridBagLayout()); p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDA),titulo,
                TitledBorder.LEFT,TitledBorder.TOP,new Font("SansSerif",Font.BOLD,12),AZUL),
            new EmptyBorder(8,12,8,12)));
        GridBagConstraints g=new GridBagConstraints(); g.insets=new Insets(5,4,5,4); g.anchor=GridBagConstraints.WEST;
        for(int i=0;i<rot.length;i++){
            g.gridx=0; g.gridy=i; g.fill=GridBagConstraints.NONE; g.weightx=0;
            p.add(new JLabel(rot[i]),g);
            g.gridx=1; g.fill=GridBagConstraints.HORIZONTAL; g.weightx=1;
            p.add(campos[i],g);
        }
        g.gridx=0; g.gridy=rot.length; g.gridwidth=2; g.weighty=1; g.fill=GridBagConstraints.VERTICAL;
        p.add(Box.createVerticalGlue(),g); return p;
    }

    private JTextField campo() {
        JTextField tf=new JTextField(16); tf.setEditable(false);
        tf.setBackground(new Color(252,252,255)); tf.setFont(new Font("Monospaced",Font.PLAIN,12));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDA), new EmptyBorder(2,6,2,6)));
        return tf;
    }

    private void carregarEstados() {
        try {
            List<UnidadeFederacao> lista=controller.getTodasUFs();
            carregando=true; cmbUF.removeAllItems();
            for(UnidadeFederacao uf:lista) cmbUF.addItem(uf);
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this,"Erro ao conectar ao banco:\n"+ex.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
        } finally {
            carregando=false;
            if(cmbUF.getItemCount()>0) cmbUF.setSelectedIndex(0);
        }
    }

    private void aoSelecionarUF(ActionEvent e) {
        if(carregando) return;
        UnidadeFederacao uf=(UnidadeFederacao)cmbUF.getSelectedItem();
        if(uf==null) return;
        txtMediaSaude.setText(fmt(uf.getMediaSaude())); txtMediaEducacao.setText(fmt(uf.getMediaEducacao()));
        txtMediaRenda.setText(fmt(uf.getMediaRenda())); txtMediaIfdm.setText(fmt(uf.getMediaIfdm()));
        try {
            List<Cidade> cidades=controller.getCidadesPorUF(uf.getId());
            carregando=true; cmbCidade.removeAllItems();
            for(Cidade c:cidades) cmbCidade.addItem(c);
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this,"Erro ao carregar municipios:\n"+ex.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
        } finally {
            carregando=false;
            if(cmbCidade.getItemCount()>0) cmbCidade.setSelectedIndex(0);
        }
    }

    private void aoSelecionarCidade(ActionEvent e) {
        if(carregando) return;
        Cidade c=(Cidade)cmbCidade.getSelectedItem();
        if(c==null) return;
        txtSaude.setText(fmt(c.getSaude())); txtEducacao.setText(fmt(c.getEducacao()));
        txtRenda.setText(fmt(c.getRenda())); txtIfdm.setText(fmt(c.getIfdm()));
        txtRankNac.setText(c.getRankingNacional()+"°"); txtRankEst.setText(c.getRankingEstadual()+"°");
    }

    private String fmt(double v){ return v==0.0?"—":String.format("%.6f",v); }
}
