/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.List;
import javax.faces.model.ListDataModel;
import model.Alunos;

import org.primefaces.model.SelectableDataModel;

public class AlunosDataModel extends ListDataModel implements SelectableDataModel<Alunos> {

    public AlunosDataModel() {
    }

    public AlunosDataModel(List<Alunos> data) {
        super(data);
    }

    @Override
    public Alunos getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  

        List<Alunos> alunos = (List<Alunos>) getWrappedData();

        for (Alunos aluno : alunos) {
            if (aluno.getIdAluno().equals(rowKey)) {
                return aluno;
            }
        }

        return null;
    }

    @Override
    public Object getRowKey(Alunos aluno) {
        return aluno.getIdAluno();
    }

}
