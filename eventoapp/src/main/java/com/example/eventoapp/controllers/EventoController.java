package com.example.eventoapp.controllers;


import com.example.eventoapp.models.Convidado;
import com.example.eventoapp.models.Evento;
import com.example.eventoapp.repository.ConvidadoRepository;
import com.example.eventoapp.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
public class EventoController {

    //injecao de depencias...
    @Autowired
    private EventoRepository eventoRepository;
    @Autowired
    private ConvidadoRepository convidadoRepository;

    @RequestMapping(value = "/cadastrarEvento", method = RequestMethod.GET)
    public String form(){
        return "FormEvento";
    }

    @RequestMapping(value = "/cadastrarEvento", method = RequestMethod.POST)
    public String form(Evento evento, BindingResult result, RedirectAttributes attributes){
        //verificacao de erros
        if(result.hasErrors()){
            attributes.addFlashAttribute("error","Verifique os campos!");// flash
            return "redirect:/cadastrarEvento";
        }

        eventoRepository.save(evento); //salvando o evento no banco de dados
        attributes.addFlashAttribute("sucess","Evento Adicionado!");// flash
        return "redirect:/cadastrarEvento";
    }

    @RequestMapping("/eventos")
    public ModelAndView listaEventos(){
        ModelAndView modelAndView = new ModelAndView("index");
        Iterable<Evento> eventos = eventoRepository.findAll();
        modelAndView.addObject("eventos",eventos);
        return modelAndView;

    }

    @RequestMapping("/deletarEvento")
    public String deletarEvento(long id){
        Evento evento = eventoRepository.findById(id);
        eventoRepository.delete(evento);
        return "redirect:/eventos";
    }

    @RequestMapping("/deletarConvidado")
    public String deletarConvidado(String cpf){
        Convidado convidado = convidadoRepository.findByCpf(cpf);
        convidadoRepository.delete(convidado);
        Evento evento = convidado.getEvento();
        return "redirect:/"+evento.getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView detalhesEvento(@PathVariable("id") long id){
        ModelAndView modelAndView = new ModelAndView("detalhesEvento");
        Evento evento = eventoRepository.findById(id);
        modelAndView.addObject("evento", evento);
        //Listando os usuarios pelo evento
        Iterable<Convidado> convidados = convidadoRepository.findByEvento(evento);
        modelAndView.addObject("convidados", convidados);

        return modelAndView;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String detalhesEvento(@PathVariable("id") long id, @Valid Convidado convidado, BindingResult result, RedirectAttributes attributes){
        //verificacao de erros
        if(result.hasErrors()){
            attributes.addFlashAttribute("error","Verifique os campos!");// flash
            return "redirect:/{id}";
        }

        Evento evento = eventoRepository.findById(id);
        convidado.setEvento(evento);
        convidadoRepository.save(convidado);
        attributes.addFlashAttribute("sucess","Convidado Adicionado!");// flash

        return "redirect:/{id}";
    }
}
