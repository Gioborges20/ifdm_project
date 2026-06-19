package ifdm.model;
public class Cidade implements Comparable<Cidade> {
    private int id, codMunic, ufId, rankingNacional, rankingEstadual;
    private String nome, siglaUf;
    private double saude, educacao, renda, ifdm;
    public Cidade() {}
    public int getId(){return id;} public void setId(int v){id=v;}
    public int getCodMunic(){return codMunic;} public void setCodMunic(int v){codMunic=v;}
    public String getNome(){return nome;} public void setNome(String v){nome=v;}
    public int getUfId(){return ufId;} public void setUfId(int v){ufId=v;}
    public String getSiglaUf(){return siglaUf;} public void setSiglaUf(String v){siglaUf=v;}
    public double getSaude(){return saude;} public void setSaude(double v){saude=v;}
    public double getEducacao(){return educacao;} public void setEducacao(double v){educacao=v;}
    public double getRenda(){return renda;} public void setRenda(double v){renda=v;}
    public double getIfdm(){return ifdm;} public void setIfdm(double v){ifdm=v;}
    public int getRankingNacional(){return rankingNacional;} public void setRankingNacional(int v){rankingNacional=v;}
    public int getRankingEstadual(){return rankingEstadual;} public void setRankingEstadual(int v){rankingEstadual=v;}
    @Override public String toString(){return nome;}
    @Override public int compareTo(Cidade o){return nome.compareTo(o.nome);}
}
