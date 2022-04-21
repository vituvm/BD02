package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Usuario;
import com.mycompany.myapp.domain.enumeration.TipoUsuario;
import com.mycompany.myapp.repository.UsuarioRepository;
import com.mycompany.myapp.service.criteria.UsuarioCriteria;
import com.mycompany.myapp.service.dto.UsuarioDTO;
import com.mycompany.myapp.service.mapper.UsuarioMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UsuarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UsuarioResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CPF = "AAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_NASCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_NASCIMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_NASCIMENTO = LocalDate.ofEpochDay(-1L);

    private static final TipoUsuario DEFAULT_TIPO = TipoUsuario.ALUNO;
    private static final TipoUsuario UPDATED_TIPO = TipoUsuario.PROFESSOR;

    private static final ZonedDateTime DEFAULT_CRIACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CRIACAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CRIACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/usuarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUsuarioMockMvc;

    private Usuario usuario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createEntity(EntityManager em) {
        Usuario usuario = new Usuario()
            .nome(DEFAULT_NOME)
            .cpf(DEFAULT_CPF)
            .dataNascimento(DEFAULT_DATA_NASCIMENTO)
            .tipo(DEFAULT_TIPO)
            .criacao(DEFAULT_CRIACAO);
        return usuario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createUpdatedEntity(EntityManager em) {
        Usuario usuario = new Usuario()
            .nome(UPDATED_NOME)
            .cpf(UPDATED_CPF)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .tipo(UPDATED_TIPO)
            .criacao(UPDATED_CRIACAO);
        return usuario;
    }

    @BeforeEach
    public void initTest() {
        usuario = createEntity(em);
    }

    @Test
    @Transactional
    void createUsuario() throws Exception {
        int databaseSizeBeforeCreate = usuarioRepository.findAll().size();
        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);
        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isCreated());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeCreate + 1);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testUsuario.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testUsuario.getDataNascimento()).isEqualTo(DEFAULT_DATA_NASCIMENTO);
        assertThat(testUsuario.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testUsuario.getCriacao()).isEqualTo(DEFAULT_CRIACAO);
    }

    @Test
    @Transactional
    void createUsuarioWithExistingId() throws Exception {
        // Create the Usuario with an existing ID
        usuario.setId(1L);
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        int databaseSizeBeforeCreate = usuarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().size();
        // set the field null
        usuario.setNome(null);

        // Create the Usuario, which fails.
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isBadRequest());

        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUsuarios() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].criacao").value(hasItem(sameInstant(DEFAULT_CRIACAO))));
    }

    @Test
    @Transactional
    void getUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get the usuario
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL_ID, usuario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(usuario.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF))
            .andExpect(jsonPath("$.dataNascimento").value(DEFAULT_DATA_NASCIMENTO.toString()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.criacao").value(sameInstant(DEFAULT_CRIACAO)));
    }

    @Test
    @Transactional
    void getUsuariosByIdFiltering() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        Long id = usuario.getId();

        defaultUsuarioShouldBeFound("id.equals=" + id);
        defaultUsuarioShouldNotBeFound("id.notEquals=" + id);

        defaultUsuarioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUsuarioShouldNotBeFound("id.greaterThan=" + id);

        defaultUsuarioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUsuarioShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUsuariosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nome equals to DEFAULT_NOME
        defaultUsuarioShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the usuarioList where nome equals to UPDATED_NOME
        defaultUsuarioShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllUsuariosByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nome not equals to DEFAULT_NOME
        defaultUsuarioShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the usuarioList where nome not equals to UPDATED_NOME
        defaultUsuarioShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllUsuariosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultUsuarioShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the usuarioList where nome equals to UPDATED_NOME
        defaultUsuarioShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllUsuariosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nome is not null
        defaultUsuarioShouldBeFound("nome.specified=true");

        // Get all the usuarioList where nome is null
        defaultUsuarioShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByNomeContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nome contains DEFAULT_NOME
        defaultUsuarioShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the usuarioList where nome contains UPDATED_NOME
        defaultUsuarioShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllUsuariosByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nome does not contain DEFAULT_NOME
        defaultUsuarioShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the usuarioList where nome does not contain UPDATED_NOME
        defaultUsuarioShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllUsuariosByCpfIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where cpf equals to DEFAULT_CPF
        defaultUsuarioShouldBeFound("cpf.equals=" + DEFAULT_CPF);

        // Get all the usuarioList where cpf equals to UPDATED_CPF
        defaultUsuarioShouldNotBeFound("cpf.equals=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllUsuariosByCpfIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where cpf not equals to DEFAULT_CPF
        defaultUsuarioShouldNotBeFound("cpf.notEquals=" + DEFAULT_CPF);

        // Get all the usuarioList where cpf not equals to UPDATED_CPF
        defaultUsuarioShouldBeFound("cpf.notEquals=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllUsuariosByCpfIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where cpf in DEFAULT_CPF or UPDATED_CPF
        defaultUsuarioShouldBeFound("cpf.in=" + DEFAULT_CPF + "," + UPDATED_CPF);

        // Get all the usuarioList where cpf equals to UPDATED_CPF
        defaultUsuarioShouldNotBeFound("cpf.in=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllUsuariosByCpfIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where cpf is not null
        defaultUsuarioShouldBeFound("cpf.specified=true");

        // Get all the usuarioList where cpf is null
        defaultUsuarioShouldNotBeFound("cpf.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByCpfContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where cpf contains DEFAULT_CPF
        defaultUsuarioShouldBeFound("cpf.contains=" + DEFAULT_CPF);

        // Get all the usuarioList where cpf contains UPDATED_CPF
        defaultUsuarioShouldNotBeFound("cpf.contains=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllUsuariosByCpfNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where cpf does not contain DEFAULT_CPF
        defaultUsuarioShouldNotBeFound("cpf.doesNotContain=" + DEFAULT_CPF);

        // Get all the usuarioList where cpf does not contain UPDATED_CPF
        defaultUsuarioShouldBeFound("cpf.doesNotContain=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllUsuariosByDataNascimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where dataNascimento equals to DEFAULT_DATA_NASCIMENTO
        defaultUsuarioShouldBeFound("dataNascimento.equals=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the usuarioList where dataNascimento equals to UPDATED_DATA_NASCIMENTO
        defaultUsuarioShouldNotBeFound("dataNascimento.equals=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllUsuariosByDataNascimentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where dataNascimento not equals to DEFAULT_DATA_NASCIMENTO
        defaultUsuarioShouldNotBeFound("dataNascimento.notEquals=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the usuarioList where dataNascimento not equals to UPDATED_DATA_NASCIMENTO
        defaultUsuarioShouldBeFound("dataNascimento.notEquals=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllUsuariosByDataNascimentoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where dataNascimento in DEFAULT_DATA_NASCIMENTO or UPDATED_DATA_NASCIMENTO
        defaultUsuarioShouldBeFound("dataNascimento.in=" + DEFAULT_DATA_NASCIMENTO + "," + UPDATED_DATA_NASCIMENTO);

        // Get all the usuarioList where dataNascimento equals to UPDATED_DATA_NASCIMENTO
        defaultUsuarioShouldNotBeFound("dataNascimento.in=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllUsuariosByDataNascimentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where dataNascimento is not null
        defaultUsuarioShouldBeFound("dataNascimento.specified=true");

        // Get all the usuarioList where dataNascimento is null
        defaultUsuarioShouldNotBeFound("dataNascimento.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByDataNascimentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where dataNascimento is greater than or equal to DEFAULT_DATA_NASCIMENTO
        defaultUsuarioShouldBeFound("dataNascimento.greaterThanOrEqual=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the usuarioList where dataNascimento is greater than or equal to UPDATED_DATA_NASCIMENTO
        defaultUsuarioShouldNotBeFound("dataNascimento.greaterThanOrEqual=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllUsuariosByDataNascimentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where dataNascimento is less than or equal to DEFAULT_DATA_NASCIMENTO
        defaultUsuarioShouldBeFound("dataNascimento.lessThanOrEqual=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the usuarioList where dataNascimento is less than or equal to SMALLER_DATA_NASCIMENTO
        defaultUsuarioShouldNotBeFound("dataNascimento.lessThanOrEqual=" + SMALLER_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllUsuariosByDataNascimentoIsLessThanSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where dataNascimento is less than DEFAULT_DATA_NASCIMENTO
        defaultUsuarioShouldNotBeFound("dataNascimento.lessThan=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the usuarioList where dataNascimento is less than UPDATED_DATA_NASCIMENTO
        defaultUsuarioShouldBeFound("dataNascimento.lessThan=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllUsuariosByDataNascimentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where dataNascimento is greater than DEFAULT_DATA_NASCIMENTO
        defaultUsuarioShouldNotBeFound("dataNascimento.greaterThan=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the usuarioList where dataNascimento is greater than SMALLER_DATA_NASCIMENTO
        defaultUsuarioShouldBeFound("dataNascimento.greaterThan=" + SMALLER_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllUsuariosByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where tipo equals to DEFAULT_TIPO
        defaultUsuarioShouldBeFound("tipo.equals=" + DEFAULT_TIPO);

        // Get all the usuarioList where tipo equals to UPDATED_TIPO
        defaultUsuarioShouldNotBeFound("tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllUsuariosByTipoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where tipo not equals to DEFAULT_TIPO
        defaultUsuarioShouldNotBeFound("tipo.notEquals=" + DEFAULT_TIPO);

        // Get all the usuarioList where tipo not equals to UPDATED_TIPO
        defaultUsuarioShouldBeFound("tipo.notEquals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllUsuariosByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where tipo in DEFAULT_TIPO or UPDATED_TIPO
        defaultUsuarioShouldBeFound("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO);

        // Get all the usuarioList where tipo equals to UPDATED_TIPO
        defaultUsuarioShouldNotBeFound("tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllUsuariosByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where tipo is not null
        defaultUsuarioShouldBeFound("tipo.specified=true");

        // Get all the usuarioList where tipo is null
        defaultUsuarioShouldNotBeFound("tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByCriacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where criacao equals to DEFAULT_CRIACAO
        defaultUsuarioShouldBeFound("criacao.equals=" + DEFAULT_CRIACAO);

        // Get all the usuarioList where criacao equals to UPDATED_CRIACAO
        defaultUsuarioShouldNotBeFound("criacao.equals=" + UPDATED_CRIACAO);
    }

    @Test
    @Transactional
    void getAllUsuariosByCriacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where criacao not equals to DEFAULT_CRIACAO
        defaultUsuarioShouldNotBeFound("criacao.notEquals=" + DEFAULT_CRIACAO);

        // Get all the usuarioList where criacao not equals to UPDATED_CRIACAO
        defaultUsuarioShouldBeFound("criacao.notEquals=" + UPDATED_CRIACAO);
    }

    @Test
    @Transactional
    void getAllUsuariosByCriacaoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where criacao in DEFAULT_CRIACAO or UPDATED_CRIACAO
        defaultUsuarioShouldBeFound("criacao.in=" + DEFAULT_CRIACAO + "," + UPDATED_CRIACAO);

        // Get all the usuarioList where criacao equals to UPDATED_CRIACAO
        defaultUsuarioShouldNotBeFound("criacao.in=" + UPDATED_CRIACAO);
    }

    @Test
    @Transactional
    void getAllUsuariosByCriacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where criacao is not null
        defaultUsuarioShouldBeFound("criacao.specified=true");

        // Get all the usuarioList where criacao is null
        defaultUsuarioShouldNotBeFound("criacao.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByCriacaoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where criacao is greater than or equal to DEFAULT_CRIACAO
        defaultUsuarioShouldBeFound("criacao.greaterThanOrEqual=" + DEFAULT_CRIACAO);

        // Get all the usuarioList where criacao is greater than or equal to UPDATED_CRIACAO
        defaultUsuarioShouldNotBeFound("criacao.greaterThanOrEqual=" + UPDATED_CRIACAO);
    }

    @Test
    @Transactional
    void getAllUsuariosByCriacaoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where criacao is less than or equal to DEFAULT_CRIACAO
        defaultUsuarioShouldBeFound("criacao.lessThanOrEqual=" + DEFAULT_CRIACAO);

        // Get all the usuarioList where criacao is less than or equal to SMALLER_CRIACAO
        defaultUsuarioShouldNotBeFound("criacao.lessThanOrEqual=" + SMALLER_CRIACAO);
    }

    @Test
    @Transactional
    void getAllUsuariosByCriacaoIsLessThanSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where criacao is less than DEFAULT_CRIACAO
        defaultUsuarioShouldNotBeFound("criacao.lessThan=" + DEFAULT_CRIACAO);

        // Get all the usuarioList where criacao is less than UPDATED_CRIACAO
        defaultUsuarioShouldBeFound("criacao.lessThan=" + UPDATED_CRIACAO);
    }

    @Test
    @Transactional
    void getAllUsuariosByCriacaoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where criacao is greater than DEFAULT_CRIACAO
        defaultUsuarioShouldNotBeFound("criacao.greaterThan=" + DEFAULT_CRIACAO);

        // Get all the usuarioList where criacao is greater than SMALLER_CRIACAO
        defaultUsuarioShouldBeFound("criacao.greaterThan=" + SMALLER_CRIACAO);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUsuarioShouldBeFound(String filter) throws Exception {
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].criacao").value(hasItem(sameInstant(DEFAULT_CRIACAO))));

        // Check, that the count call also returns 1
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUsuarioShouldNotBeFound(String filter) throws Exception {
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUsuario() throws Exception {
        // Get the usuario
        restUsuarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario
        Usuario updatedUsuario = usuarioRepository.findById(usuario.getId()).get();
        // Disconnect from session so that the updates on updatedUsuario are not directly saved in db
        em.detach(updatedUsuario);
        updatedUsuario
            .nome(UPDATED_NOME)
            .cpf(UPDATED_CPF)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .tipo(UPDATED_TIPO)
            .criacao(UPDATED_CRIACAO);
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(updatedUsuario);

        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usuarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testUsuario.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testUsuario.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testUsuario.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testUsuario.getCriacao()).isEqualTo(UPDATED_CRIACAO);
    }

    @Test
    @Transactional
    void putNonExistingUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usuarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUsuarioWithPatch() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario using partial update
        Usuario partialUpdatedUsuario = new Usuario();
        partialUpdatedUsuario.setId(usuario.getId());

        partialUpdatedUsuario.nome(UPDATED_NOME).cpf(UPDATED_CPF).criacao(UPDATED_CRIACAO);

        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testUsuario.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testUsuario.getDataNascimento()).isEqualTo(DEFAULT_DATA_NASCIMENTO);
        assertThat(testUsuario.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testUsuario.getCriacao()).isEqualTo(UPDATED_CRIACAO);
    }

    @Test
    @Transactional
    void fullUpdateUsuarioWithPatch() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario using partial update
        Usuario partialUpdatedUsuario = new Usuario();
        partialUpdatedUsuario.setId(usuario.getId());

        partialUpdatedUsuario
            .nome(UPDATED_NOME)
            .cpf(UPDATED_CPF)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .tipo(UPDATED_TIPO)
            .criacao(UPDATED_CRIACAO);

        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testUsuario.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testUsuario.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testUsuario.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testUsuario.getCriacao()).isEqualTo(UPDATED_CRIACAO);
    }

    @Test
    @Transactional
    void patchNonExistingUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, usuarioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeDelete = usuarioRepository.findAll().size();

        // Delete the usuario
        restUsuarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, usuario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
