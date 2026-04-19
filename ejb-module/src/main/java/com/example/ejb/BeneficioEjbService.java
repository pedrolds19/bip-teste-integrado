package com.example.ejb;

import com.example.domain.Beneficio;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;

@Stateless
public class BeneficioEjbService {

  @PersistenceContext(unitName = "bip-teste-integrado-PU")
  private EntityManager em;

  public Beneficio buscarPorId(Long id) {

    return em.find(Beneficio.class, id, LockModeType.PESSIMISTIC_WRITE);
  }

  public void remover(Long id) {

    Beneficio b = em.find(Beneficio.class, id, LockModeType.PESSIMISTIC_WRITE);

    if (b != null) {
      em.remove(b);
      em.flush();
    } else {
      throw new RuntimeException("Benefício não encontrado para exclusão.");
    }
  }

  public void transfer(Long fromId, Long toId, BigDecimal amount) {
    Beneficio from = em.find(Beneficio.class, fromId, LockModeType.PESSIMISTIC_WRITE);
    Beneficio to = em.find(Beneficio.class, toId, LockModeType.PESSIMISTIC_WRITE);

    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalStateException("O valor da transferência deve ser maior que zero.");
    }
    if (from == null || to == null) {
      throw new IllegalStateException("Conta de origem ou destino não encontrada.");
    }

    if (from.getValor().compareTo(amount) < 0) {
      throw new IllegalStateException("Saldo insuficiente para realizar a transferência.");
    }

    from.setValor(from.getValor().subtract(amount));
    to.setValor(to.getValor().add(amount));
  }

  public void salvar(Beneficio beneficio) {

    em.persist(beneficio);

  }

  public List<Beneficio> buscarTodos() {
    return em.createQuery("SELECT b FROM Beneficio b", Beneficio.class).getResultList();
  }
}

