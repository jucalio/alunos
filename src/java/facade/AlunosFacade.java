/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controller.HibernateUtil;
import javax.ejb.Stateless;
import model.Alunos;

import org.hibernate.SessionFactory;

/**
 *
 * @author charles
 */
@Stateless
public class AlunosFacade extends AbstractFacade<Alunos> {

    public AlunosFacade() {
        super(Alunos.class);
    }

    @Override
    protected SessionFactory getSessionFactory() {

        return HibernateUtil.getSessionFactory();

    }


}
