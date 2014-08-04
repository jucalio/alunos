/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import facade.PessoasFacade;
import model.Pessoas;
import util.PessoasDataModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.inject.Named;

/**
 *
 * @author charles
 */
@Named(value = "pessoasController")
@SessionScoped
public class PessoasController implements Serializable {

    private Pessoas pessoa;

    @EJB
    PessoasFacade pessoasFacade;
    private static PessoasDataModel pessoasDataModel;

    public PessoasController() {
        this.pessoa = new Pessoas();
    }

    public Pessoas getPessoa() {
        if (pessoa == null) {
            pessoa = new Pessoas();
        }
        return pessoa;
    }

    private Pessoas getPessoa(Long key) {
        return this.buscar(key);
    }

    public SelectItem[] getItemsAvaiableSelectOne() {
        return JsfUtil.getSelectItems(pessoasFacade.findAll(), true);
    }

    public void setPessoa(Pessoas pessoa) {
        this.pessoa = pessoa;
    }

    //************************************************ Data Model **********************************************************************************
    
    public PessoasDataModel getPessoasDataModel() {
        if (pessoasDataModel == null) {
            List<Pessoas> pessoas = this.listarTodas();
            pessoasDataModel = new PessoasDataModel(pessoas);
        }
        return pessoasDataModel;
    }

    public static void setPessoasDataModel(PessoasDataModel pessoasDataModel) {
        PessoasController.pessoasDataModel = pessoasDataModel;
    }

    public void recriarModelo() {
        PessoasController.pessoasDataModel = null;
    }

    //********************************************* "Prepares" das páginas **********************************************************************************************
    
    public String prepareCreate(int i) {
        pessoa = new Pessoas();
        if (i == 1) {
            return "/view/pessoas/Create";
        } else {
            return "Create";
        }
    }

    public String prepareEdit() {
        pessoa = (Pessoas) pessoasDataModel.getRowData();
        return "Edit";
    }

    public String prepareView() {
        pessoa = (Pessoas) pessoasDataModel.getRowData();
        return "View";
    }

    //*************************************************** CRUD ******************************************************************
    
    public void salvarNoBanco() {

        try {
            pessoasFacade.save(pessoa);
            JsfUtil.addSuccessMessage("Pessoa " + pessoa.getNomePessoa() + " criada com sucesso!");
            pessoa = null;
            recriarModelo();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência para salvar a pessoa " + pessoa.getNomePessoa());

        }

    }

    public Pessoas buscar(Long id) {

        return pessoasFacade.find(id);

    }

    public List<Pessoas> listarTodas() {
        return pessoasFacade.findAll();
    }

    public void delete() {
        pessoa = (Pessoas) pessoasDataModel.getRowData();
        try {
            pessoasFacade.remove(pessoa);
            pessoa = null;
            JsfUtil.addSuccessMessage("Pessoa Deletada");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência para deletar a pessoa " + pessoa.getNomePessoa());
        }
        recriarModelo();

    }

    public void editar() {
        try {
            pessoasFacade.edit(pessoa);
            JsfUtil.addSuccessMessage("Pessoa editada com sucesso!");

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro: " + e.getMessage() + " para editar a pessoa " + pessoa.getNomePessoa());
        }

    }
    
    //*************************************************************************************************************************************************

    @FacesConverter(forClass = Pessoas.class)
    public static class PessoasControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PessoasController controller = (PessoasController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "pessoasController");
            return controller.getPessoa(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Pessoas) {
                Pessoas p = (Pessoas) object;
                return getStringKey(p.getIdPessoa().setScale( 0, BigDecimal.ROUND_HALF_UP ).longValue());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Pessoas.class.getName());
            }
        }
    }

}
