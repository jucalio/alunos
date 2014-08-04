/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.List;
import javax.faces.model.ListDataModel;
import model.Pessoas;

import org.primefaces.model.SelectableDataModel;

public class PessoasDataModel extends ListDataModel implements SelectableDataModel<Pessoas> {

    public PessoasDataModel() {
    }

    public PessoasDataModel(List<Pessoas> data) {
        super(data);
    }

    @Override
    public Pessoas getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  

        List<Pessoas> pessoas = (List<Pessoas>) getWrappedData();

        for (Pessoas pessoa : pessoas) {
            if (pessoa.getIdPessoa().equals(rowKey)) {
                return pessoa;
            }
        }

        return null;
    }

    @Override
    public Object getRowKey(Pessoas pessoa) {
        return pessoa.getIdPessoa();
    }

}
