/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import facade.AlunosFacade;
import model.Alunos;
import util.AlunosDataModel;
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
@Named(value = "alunosController")
@SessionScoped
public class AlunosController implements Serializable {

    private Alunos aluno;

    @EJB
    AlunosFacade alunosFacade;
    private static AlunosDataModel alunosDataModel;

    public AlunosController() {
        this.aluno = new Alunos();
    }

    public Alunos getAluno() {
        if (aluno == null) {
            aluno = new Alunos();
        }
        return aluno;
    }

    private Alunos getAluno(Long key) {
        return this.buscar(key);
    }

    public SelectItem[] getItemsAvaiableSelectOne() {
        return JsfUtil.getSelectItems(alunosFacade.findAll(), true);
    }

    public void setAluno(Alunos aluno) {
        this.aluno = aluno;
    }

    //************************************************ Data Model **********************************************************************************
    
    public AlunosDataModel getAlunosDataModel() {
        if (alunosDataModel == null) {
            List<Alunos> alunos = this.listarTodas();
            alunosDataModel = new AlunosDataModel(alunos);
        }
        return alunosDataModel;
    }

    public static void setAlunosDataModel(AlunosDataModel alunosDataModel) {
        AlunosController.alunosDataModel = alunosDataModel;
    }

    public void recriarModelo() {
        AlunosController.alunosDataModel = null;
    }

    //********************************************* "Prepares" das páginas **********************************************************************************************
    
    public String prepareCreate(int i) {
        aluno = new Alunos();
        if (i == 1) {
            return "/view/alunos/Create";
        } else {
            return "Create";
        }
    }

    public String prepareEdit() {
        aluno = (Alunos) alunosDataModel.getRowData();
        return "Edit";
    }

    public String prepareView() {
        aluno = (Alunos) alunosDataModel.getRowData();
        return "View";
    }

    //*************************************************** CRUD ******************************************************************
    
    public void salvarNoBanco() {

        try {
            alunosFacade.save(aluno);
            JsfUtil.addSuccessMessage("Aluno " + aluno.getPessoasByIdPessoa().getNomePessoa() + " criada com sucesso!");
            aluno = null;
            recriarModelo();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência para salvar a aluno " + aluno.getPessoasByIdPessoa().getNomePessoa());

        }

    }

    public Alunos buscar(Long id) {

        return alunosFacade.find(id);

    }

    public List<Alunos> listarTodas() {
        return alunosFacade.findAll();
    }

    public void delete() {
        aluno = (Alunos) alunosDataModel.getRowData();
        try {
            alunosFacade.remove(aluno);
            aluno = null;
            JsfUtil.addSuccessMessage("Aluno Deletada");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência para deletar a aluno " + aluno.getPessoasByIdPessoa().getNomePessoa());
        }
        recriarModelo();

    }

    public void editar() {
        try {
            alunosFacade.edit(aluno);
            JsfUtil.addSuccessMessage("Aluno editada com sucesso!");

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro: " + e.getMessage() + " para editar a aluno " + aluno.getPessoasByIdPessoa().getNomePessoa());
        }

    }
    
    //*************************************************************************************************************************************************

    @FacesConverter(forClass = Alunos.class)
    public static class AlunosControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AlunosController controller = (AlunosController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "alunosController");
            return controller.getAluno(getKey(value));
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
            if (object instanceof Alunos) {
                Alunos p = (Alunos) object;
                return getStringKey(p.getIdAluno().setScale( 0, BigDecimal.ROUND_HALF_UP ).longValue());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Alunos.class.getName());
            }
        }
    }

}


