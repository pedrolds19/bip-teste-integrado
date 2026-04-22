package com.example.rest;

import com.example.domain.Beneficio;
import com.example.ejb.BeneficioEjbService;


import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.QueryParam;
import java.math.BigDecimal;
import java.util.List;

@Path("/v1/beneficios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BeneficioResource {

    @EJB
    private BeneficioEjbService beneficioService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response listarTodos() {
    List<Beneficio> lista = beneficioService.buscarTodos();
    return Response.ok(lista)
      .header("Access-Control-Allow-Origin", "*")
      .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
      .header("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization")
      .header("Content-Type", "application/json;charset=utf-8")
      .build();
  }

  @POST
  @Path("/transferir")
  public Response transferir(@QueryParam("origem") Long origem,
                             @QueryParam("destino") Long destino,
                             @QueryParam("valor") BigDecimal valor) {
    try {
      beneficioService.transfer(origem, destino, valor);

      return Response.ok()
        .header("Access-Control-Allow-Origin", "*")
        .header("Access-Control-Allow-Methods", "POST, GET, OPTIONS")
        .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
        .build();

    }  catch (Exception e) {
      String mensagemErro = "Erro desconhecido ao transferir.";
      Throwable cause = e;

      while (cause != null) {
        if (cause instanceof IllegalArgumentException || cause instanceof IllegalStateException) {
          mensagemErro = cause.getMessage();
          break;
        }
        cause = cause.getCause();

      }
      return Response.status(Response.Status.BAD_REQUEST)
        .entity("{\"erro\": \"" + mensagemErro + "\"}")
        .type(MediaType.APPLICATION_JSON) 
        .header("Access-Control-Allow-Origin", "*")
        .build();
    }

  }

  @OPTIONS
  public Response options() {
    return Response.ok()
      .header("Access-Control-Allow-Origin", "*")
      .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
      .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
      .build();
  }
  @OPTIONS
  @Path("/{id}")
  public Response optionsId() {
    return Response.ok()
      .header("Access-Control-Allow-Origin", "*")
      .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
      .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
      .build();
  }
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response criar(Beneficio beneficio) {
    try {
      beneficioService.salvar(beneficio);
      return Response.status(Response.Status.CREATED)
        .entity(beneficio)
        .header("Access-Control-Allow-Origin", "*") 
        .header("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept")
        .header("Access-Control-Allow-Methods", "POST, GET, OPTIONS")
        .build();
    } catch (Exception e) {
      e.printStackTrace(); 
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity("Erro: " + e.getMessage())
        .header("Access-Control-Allow-Origin", "*") 
        .build();
    }
  }

  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response remover(@PathParam("id") Long id) {
    try {
      beneficioService.remover(id);

      return Response.ok("{\"mensagem\": \"Ativo removido com sucesso\"}")
        .header("Access-Control-Allow-Origin", "*")
        .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
        .build();

    } catch (Exception e) {
      e.printStackTrace();
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity("{\"erro\": \"" + e.getMessage() + "\"}")
        .header("Access-Control-Allow-Origin", "*")
        .build();
    }
  }
  @GET
  @Path("/{id}")
  public Response buscarPorId(@PathParam("id") Long id) {
    Beneficio b = beneficioService.buscarPorId(id);
    if (b == null) {
      return Response.status(Response.Status.NOT_FOUND)
        .header("Access-Control-Allow-Origin", "*")
        .build();
    }
    return Response.ok(b)
      .header("Access-Control-Allow-Origin", "*")
      .build();
  }
}
