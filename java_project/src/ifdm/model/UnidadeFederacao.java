package ifdm.model;

public class UnidadeFederacao implements Comparable<UnidadeFederacao> {
    private int id; private String uf, descricao;
    private double mediaSaude, mediaEducacao, mediaRenda, mediaIfdm;
    public UnidadeFederacao() {}
    public int getId(){return id;} public void setId(int v){id=v;}
    public String getUf(){return uf;} public void setUf(String v){uf=v;}
    public String getDescricao(){return descricao;} public void setDescricao(String v){descricao=v;}
    public double getMediaSaude(){return mediaSaude;} public void setMediaSaude(double v){mediaSaude=v;}
    public double getMediaEducacao(){return mediaEducacao;} public void setMediaEducacao(double v){mediaEducacao=v;}
    public double getMediaRenda(){return mediaRenda;} public void setMediaRenda(double v){mediaRenda=v;}
    public double getMediaIfdm(){return mediaIfdm;} public void setMediaIfdm(double v){mediaIfdm=v;}
    @Override public String toString(){return descricao;}
    @Override public int compareTo(UnidadeFederacao o){return descricao.compareTo(o.descricao);}
}
