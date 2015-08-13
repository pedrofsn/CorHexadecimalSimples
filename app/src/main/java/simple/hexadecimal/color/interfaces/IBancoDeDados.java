package simple.hexadecimal.color.interfaces;

import java.util.List;

import simple.hexadecimal.color.model.Cor;

public interface IBancoDeDados {

    public void createCor(Cor object);

    public Cor readCor(int id);

    public List<Cor> readAllCor();

    public int updateCor(Cor object);

    public void deleteCor(String hex);

    public int getCountCor();

    public Cor searchCor(String corPesuisada);

    public boolean hasCor(String corPesuisada);

}
