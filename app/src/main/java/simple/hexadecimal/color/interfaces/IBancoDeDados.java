package simple.hexadecimal.color.interfaces;

import java.util.List;

import simple.hexadecimal.color.model.Cor;

public interface IBancoDeDados {

    void createCor(Cor object);

    Cor readCor(int id);

    List<Cor> readAllCor();

    int updateCor(Cor object);

    void deleteCor(String hex);

    int getCountCor();

    Cor searchCor(String corPesuisada);

    boolean hasCor(String corPesuisada);

}
