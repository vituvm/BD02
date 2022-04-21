package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Compra;
import com.mycompany.myapp.domain.Curso;
import com.mycompany.myapp.domain.Usuario;
import com.mycompany.myapp.domain.enumeration.EstadoTransacao;
import com.mycompany.myapp.domain.enumeration.Pagamento;
import com.mycompany.myapp.repository.CompraRepository;
import com.mycompany.myapp.service.CompraService;
import com.mycompany.myapp.service.criteria.CompraCriteria;
import com.mycompany.myapp.service.dto.CompraDTO;
import com.mycompany.myapp.service.mapper.CompraMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CompraResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CompraResourceIT {

    private static final Double DEFAULT_PERCENTUAL_DESCONTO = 1D;
    private static final Double UPDATED_PERCENTUAL_DESCONTO = 2D;
    private static final Double SMALLER_PERCENTUAL_DESCONTO = 1D - 1D;

    private static final Double DEFAULT_VALOR_FINAL = 1D;
    private static final Double UPDATED_VALOR_FINAL = 2D;
    private static final Double SMALLER_VALOR_FINAL = 1D - 1D;

    private static final ZonedDateTime DEFAULT_DATA_CRIACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_CRIACAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_CRIACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Pagamento DEFAULT_FORMA_PAGAMENTO = Pagamento.BOLETO;
    private static final Pagamento UPDATED_FORMA_PAGAMENTO = Pagamento.CARTAO_CREDITO;

    private static final EstadoTransacao DEFAULT_ESTADO = EstadoTransacao.CRIADO;
    private static final EstadoTransacao UPDATED_ESTADO = EstadoTransacao.AGUARDANDO_PAGAMENTO;

    private static final String ENTITY_API_URL = "/api/compras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompraRepository compraRepository;

    @Mock
    private CompraRepository compraRepositoryMock;

    @Autowired
    private CompraMapper compraMapper;

    @Mock
    private CompraService compraServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompraMockMvc;

    private Compra compra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compra createEntity(EntityManager em) {
        Compra compra = new Compra()
            .percentualDesconto(DEFAULT_PERCENTUAL_DESCONTO)
            .valorFinal(DEFAULT_VALOR_FINAL)
            .dataCriacao(DEFAULT_DATA_CRIACAO)
            .formaPagamento(DEFAULT_FORMA_PAGAMENTO)
            .estado(DEFAULT_ESTADO);
        return compra;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compra createUpdatedEntity(EntityManager em) {
        Compra compra = new Compra()
            .percentualDesconto(UPDATED_PERCENTUAL_DESCONTO)
            .valorFinal(UPDATED_VALOR_FINAL)
            .dataCriacao(UPDATED_DATA_CRIACAO)
            .formaPagamento(UPDATED_FORMA_PAGAMENTO)
            .estado(UPDATED_ESTADO);
        return compra;
    }

    @BeforeEach
    public void initTest() {
        compra = createEntity(em);
    }

    @Test
    @Transactional
    void createCompra() throws Exception {
        int databaseSizeBeforeCreate = compraRepository.findAll().size();
        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);
        restCompraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compraDTO)))
            .andExpect(status().isCreated());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeCreate + 1);
        Compra testCompra = compraList.get(compraList.size() - 1);
        assertThat(testCompra.getPercentualDesconto()).isEqualTo(DEFAULT_PERCENTUAL_DESCONTO);
        assertThat(testCompra.getValorFinal()).isEqualTo(DEFAULT_VALOR_FINAL);
        assertThat(testCompra.getDataCriacao()).isEqualTo(DEFAULT_DATA_CRIACAO);
        assertThat(testCompra.getFormaPagamento()).isEqualTo(DEFAULT_FORMA_PAGAMENTO);
        assertThat(testCompra.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createCompraWithExistingId() throws Exception {
        // Create the Compra with an existing ID
        compra.setId(1L);
        CompraDTO compraDTO = compraMapper.toDto(compra);

        int databaseSizeBeforeCreate = compraRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compraDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompras() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList
        restCompraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compra.getId().intValue())))
            .andExpect(jsonPath("$.[*].percentualDesconto").value(hasItem(DEFAULT_PERCENTUAL_DESCONTO.doubleValue())))
            .andExpect(jsonPath("$.[*].valorFinal").value(hasItem(DEFAULT_VALOR_FINAL.doubleValue())))
            .andExpect(jsonPath("$.[*].dataCriacao").value(hasItem(sameInstant(DEFAULT_DATA_CRIACAO))))
            .andExpect(jsonPath("$.[*].formaPagamento").value(hasItem(DEFAULT_FORMA_PAGAMENTO.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllComprasWithEagerRelationshipsIsEnabled() throws Exception {
        when(compraServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCompraMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(compraServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllComprasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(compraServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCompraMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(compraServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCompra() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get the compra
        restCompraMockMvc
            .perform(get(ENTITY_API_URL_ID, compra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compra.getId().intValue()))
            .andExpect(jsonPath("$.percentualDesconto").value(DEFAULT_PERCENTUAL_DESCONTO.doubleValue()))
            .andExpect(jsonPath("$.valorFinal").value(DEFAULT_VALOR_FINAL.doubleValue()))
            .andExpect(jsonPath("$.dataCriacao").value(sameInstant(DEFAULT_DATA_CRIACAO)))
            .andExpect(jsonPath("$.formaPagamento").value(DEFAULT_FORMA_PAGAMENTO.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    void getComprasByIdFiltering() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        Long id = compra.getId();

        defaultCompraShouldBeFound("id.equals=" + id);
        defaultCompraShouldNotBeFound("id.notEquals=" + id);

        defaultCompraShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCompraShouldNotBeFound("id.greaterThan=" + id);

        defaultCompraShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCompraShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllComprasByPercentualDescontoIsEqualToSomething() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where percentualDesconto equals to DEFAULT_PERCENTUAL_DESCONTO
        defaultCompraShouldBeFound("percentualDesconto.equals=" + DEFAULT_PERCENTUAL_DESCONTO);

        // Get all the compraList where percentualDesconto equals to UPDATED_PERCENTUAL_DESCONTO
        defaultCompraShouldNotBeFound("percentualDesconto.equals=" + UPDATED_PERCENTUAL_DESCONTO);
    }

    @Test
    @Transactional
    void getAllComprasByPercentualDescontoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where percentualDesconto not equals to DEFAULT_PERCENTUAL_DESCONTO
        defaultCompraShouldNotBeFound("percentualDesconto.notEquals=" + DEFAULT_PERCENTUAL_DESCONTO);

        // Get all the compraList where percentualDesconto not equals to UPDATED_PERCENTUAL_DESCONTO
        defaultCompraShouldBeFound("percentualDesconto.notEquals=" + UPDATED_PERCENTUAL_DESCONTO);
    }

    @Test
    @Transactional
    void getAllComprasByPercentualDescontoIsInShouldWork() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where percentualDesconto in DEFAULT_PERCENTUAL_DESCONTO or UPDATED_PERCENTUAL_DESCONTO
        defaultCompraShouldBeFound("percentualDesconto.in=" + DEFAULT_PERCENTUAL_DESCONTO + "," + UPDATED_PERCENTUAL_DESCONTO);

        // Get all the compraList where percentualDesconto equals to UPDATED_PERCENTUAL_DESCONTO
        defaultCompraShouldNotBeFound("percentualDesconto.in=" + UPDATED_PERCENTUAL_DESCONTO);
    }

    @Test
    @Transactional
    void getAllComprasByPercentualDescontoIsNullOrNotNull() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where percentualDesconto is not null
        defaultCompraShouldBeFound("percentualDesconto.specified=true");

        // Get all the compraList where percentualDesconto is null
        defaultCompraShouldNotBeFound("percentualDesconto.specified=false");
    }

    @Test
    @Transactional
    void getAllComprasByPercentualDescontoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where percentualDesconto is greater than or equal to DEFAULT_PERCENTUAL_DESCONTO
        defaultCompraShouldBeFound("percentualDesconto.greaterThanOrEqual=" + DEFAULT_PERCENTUAL_DESCONTO);

        // Get all the compraList where percentualDesconto is greater than or equal to UPDATED_PERCENTUAL_DESCONTO
        defaultCompraShouldNotBeFound("percentualDesconto.greaterThanOrEqual=" + UPDATED_PERCENTUAL_DESCONTO);
    }

    @Test
    @Transactional
    void getAllComprasByPercentualDescontoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where percentualDesconto is less than or equal to DEFAULT_PERCENTUAL_DESCONTO
        defaultCompraShouldBeFound("percentualDesconto.lessThanOrEqual=" + DEFAULT_PERCENTUAL_DESCONTO);

        // Get all the compraList where percentualDesconto is less than or equal to SMALLER_PERCENTUAL_DESCONTO
        defaultCompraShouldNotBeFound("percentualDesconto.lessThanOrEqual=" + SMALLER_PERCENTUAL_DESCONTO);
    }

    @Test
    @Transactional
    void getAllComprasByPercentualDescontoIsLessThanSomething() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where percentualDesconto is less than DEFAULT_PERCENTUAL_DESCONTO
        defaultCompraShouldNotBeFound("percentualDesconto.lessThan=" + DEFAULT_PERCENTUAL_DESCONTO);

        // Get all the compraList where percentualDesconto is less than UPDATED_PERCENTUAL_DESCONTO
        defaultCompraShouldBeFound("percentualDesconto.lessThan=" + UPDATED_PERCENTUAL_DESCONTO);
    }

    @Test
    @Transactional
    void getAllComprasByPercentualDescontoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where percentualDesconto is greater than DEFAULT_PERCENTUAL_DESCONTO
        defaultCompraShouldNotBeFound("percentualDesconto.greaterThan=" + DEFAULT_PERCENTUAL_DESCONTO);

        // Get all the compraList where percentualDesconto is greater than SMALLER_PERCENTUAL_DESCONTO
        defaultCompraShouldBeFound("percentualDesconto.greaterThan=" + SMALLER_PERCENTUAL_DESCONTO);
    }

    @Test
    @Transactional
    void getAllComprasByValorFinalIsEqualToSomething() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where valorFinal equals to DEFAULT_VALOR_FINAL
        defaultCompraShouldBeFound("valorFinal.equals=" + DEFAULT_VALOR_FINAL);

        // Get all the compraList where valorFinal equals to UPDATED_VALOR_FINAL
        defaultCompraShouldNotBeFound("valorFinal.equals=" + UPDATED_VALOR_FINAL);
    }

    @Test
    @Transactional
    void getAllComprasByValorFinalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where valorFinal not equals to DEFAULT_VALOR_FINAL
        defaultCompraShouldNotBeFound("valorFinal.notEquals=" + DEFAULT_VALOR_FINAL);

        // Get all the compraList where valorFinal not equals to UPDATED_VALOR_FINAL
        defaultCompraShouldBeFound("valorFinal.notEquals=" + UPDATED_VALOR_FINAL);
    }

    @Test
    @Transactional
    void getAllComprasByValorFinalIsInShouldWork() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where valorFinal in DEFAULT_VALOR_FINAL or UPDATED_VALOR_FINAL
        defaultCompraShouldBeFound("valorFinal.in=" + DEFAULT_VALOR_FINAL + "," + UPDATED_VALOR_FINAL);

        // Get all the compraList where valorFinal equals to UPDATED_VALOR_FINAL
        defaultCompraShouldNotBeFound("valorFinal.in=" + UPDATED_VALOR_FINAL);
    }

    @Test
    @Transactional
    void getAllComprasByValorFinalIsNullOrNotNull() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where valorFinal is not null
        defaultCompraShouldBeFound("valorFinal.specified=true");

        // Get all the compraList where valorFinal is null
        defaultCompraShouldNotBeFound("valorFinal.specified=false");
    }

    @Test
    @Transactional
    void getAllComprasByValorFinalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where valorFinal is greater than or equal to DEFAULT_VALOR_FINAL
        defaultCompraShouldBeFound("valorFinal.greaterThanOrEqual=" + DEFAULT_VALOR_FINAL);

        // Get all the compraList where valorFinal is greater than or equal to UPDATED_VALOR_FINAL
        defaultCompraShouldNotBeFound("valorFinal.greaterThanOrEqual=" + UPDATED_VALOR_FINAL);
    }

    @Test
    @Transactional
    void getAllComprasByValorFinalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where valorFinal is less than or equal to DEFAULT_VALOR_FINAL
        defaultCompraShouldBeFound("valorFinal.lessThanOrEqual=" + DEFAULT_VALOR_FINAL);

        // Get all the compraList where valorFinal is less than or equal to SMALLER_VALOR_FINAL
        defaultCompraShouldNotBeFound("valorFinal.lessThanOrEqual=" + SMALLER_VALOR_FINAL);
    }

    @Test
    @Transactional
    void getAllComprasByValorFinalIsLessThanSomething() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where valorFinal is less than DEFAULT_VALOR_FINAL
        defaultCompraShouldNotBeFound("valorFinal.lessThan=" + DEFAULT_VALOR_FINAL);

        // Get all the compraList where valorFinal is less than UPDATED_VALOR_FINAL
        defaultCompraShouldBeFound("valorFinal.lessThan=" + UPDATED_VALOR_FINAL);
    }

    @Test
    @Transactional
    void getAllComprasByValorFinalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where valorFinal is greater than DEFAULT_VALOR_FINAL
        defaultCompraShouldNotBeFound("valorFinal.greaterThan=" + DEFAULT_VALOR_FINAL);

        // Get all the compraList where valorFinal is greater than SMALLER_VALOR_FINAL
        defaultCompraShouldBeFound("valorFinal.greaterThan=" + SMALLER_VALOR_FINAL);
    }

    @Test
    @Transactional
    void getAllComprasByDataCriacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where dataCriacao equals to DEFAULT_DATA_CRIACAO
        defaultCompraShouldBeFound("dataCriacao.equals=" + DEFAULT_DATA_CRIACAO);

        // Get all the compraList where dataCriacao equals to UPDATED_DATA_CRIACAO
        defaultCompraShouldNotBeFound("dataCriacao.equals=" + UPDATED_DATA_CRIACAO);
    }

    @Test
    @Transactional
    void getAllComprasByDataCriacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where dataCriacao not equals to DEFAULT_DATA_CRIACAO
        defaultCompraShouldNotBeFound("dataCriacao.notEquals=" + DEFAULT_DATA_CRIACAO);

        // Get all the compraList where dataCriacao not equals to UPDATED_DATA_CRIACAO
        defaultCompraShouldBeFound("dataCriacao.notEquals=" + UPDATED_DATA_CRIACAO);
    }

    @Test
    @Transactional
    void getAllComprasByDataCriacaoIsInShouldWork() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where dataCriacao in DEFAULT_DATA_CRIACAO or UPDATED_DATA_CRIACAO
        defaultCompraShouldBeFound("dataCriacao.in=" + DEFAULT_DATA_CRIACAO + "," + UPDATED_DATA_CRIACAO);

        // Get all the compraList where dataCriacao equals to UPDATED_DATA_CRIACAO
        defaultCompraShouldNotBeFound("dataCriacao.in=" + UPDATED_DATA_CRIACAO);
    }

    @Test
    @Transactional
    void getAllComprasByDataCriacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where dataCriacao is not null
        defaultCompraShouldBeFound("dataCriacao.specified=true");

        // Get all the compraList where dataCriacao is null
        defaultCompraShouldNotBeFound("dataCriacao.specified=false");
    }

    @Test
    @Transactional
    void getAllComprasByDataCriacaoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where dataCriacao is greater than or equal to DEFAULT_DATA_CRIACAO
        defaultCompraShouldBeFound("dataCriacao.greaterThanOrEqual=" + DEFAULT_DATA_CRIACAO);

        // Get all the compraList where dataCriacao is greater than or equal to UPDATED_DATA_CRIACAO
        defaultCompraShouldNotBeFound("dataCriacao.greaterThanOrEqual=" + UPDATED_DATA_CRIACAO);
    }

    @Test
    @Transactional
    void getAllComprasByDataCriacaoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where dataCriacao is less than or equal to DEFAULT_DATA_CRIACAO
        defaultCompraShouldBeFound("dataCriacao.lessThanOrEqual=" + DEFAULT_DATA_CRIACAO);

        // Get all the compraList where dataCriacao is less than or equal to SMALLER_DATA_CRIACAO
        defaultCompraShouldNotBeFound("dataCriacao.lessThanOrEqual=" + SMALLER_DATA_CRIACAO);
    }

    @Test
    @Transactional
    void getAllComprasByDataCriacaoIsLessThanSomething() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where dataCriacao is less than DEFAULT_DATA_CRIACAO
        defaultCompraShouldNotBeFound("dataCriacao.lessThan=" + DEFAULT_DATA_CRIACAO);

        // Get all the compraList where dataCriacao is less than UPDATED_DATA_CRIACAO
        defaultCompraShouldBeFound("dataCriacao.lessThan=" + UPDATED_DATA_CRIACAO);
    }

    @Test
    @Transactional
    void getAllComprasByDataCriacaoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where dataCriacao is greater than DEFAULT_DATA_CRIACAO
        defaultCompraShouldNotBeFound("dataCriacao.greaterThan=" + DEFAULT_DATA_CRIACAO);

        // Get all the compraList where dataCriacao is greater than SMALLER_DATA_CRIACAO
        defaultCompraShouldBeFound("dataCriacao.greaterThan=" + SMALLER_DATA_CRIACAO);
    }

    @Test
    @Transactional
    void getAllComprasByFormaPagamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where formaPagamento equals to DEFAULT_FORMA_PAGAMENTO
        defaultCompraShouldBeFound("formaPagamento.equals=" + DEFAULT_FORMA_PAGAMENTO);

        // Get all the compraList where formaPagamento equals to UPDATED_FORMA_PAGAMENTO
        defaultCompraShouldNotBeFound("formaPagamento.equals=" + UPDATED_FORMA_PAGAMENTO);
    }

    @Test
    @Transactional
    void getAllComprasByFormaPagamentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where formaPagamento not equals to DEFAULT_FORMA_PAGAMENTO
        defaultCompraShouldNotBeFound("formaPagamento.notEquals=" + DEFAULT_FORMA_PAGAMENTO);

        // Get all the compraList where formaPagamento not equals to UPDATED_FORMA_PAGAMENTO
        defaultCompraShouldBeFound("formaPagamento.notEquals=" + UPDATED_FORMA_PAGAMENTO);
    }

    @Test
    @Transactional
    void getAllComprasByFormaPagamentoIsInShouldWork() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where formaPagamento in DEFAULT_FORMA_PAGAMENTO or UPDATED_FORMA_PAGAMENTO
        defaultCompraShouldBeFound("formaPagamento.in=" + DEFAULT_FORMA_PAGAMENTO + "," + UPDATED_FORMA_PAGAMENTO);

        // Get all the compraList where formaPagamento equals to UPDATED_FORMA_PAGAMENTO
        defaultCompraShouldNotBeFound("formaPagamento.in=" + UPDATED_FORMA_PAGAMENTO);
    }

    @Test
    @Transactional
    void getAllComprasByFormaPagamentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where formaPagamento is not null
        defaultCompraShouldBeFound("formaPagamento.specified=true");

        // Get all the compraList where formaPagamento is null
        defaultCompraShouldNotBeFound("formaPagamento.specified=false");
    }

    @Test
    @Transactional
    void getAllComprasByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where estado equals to DEFAULT_ESTADO
        defaultCompraShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the compraList where estado equals to UPDATED_ESTADO
        defaultCompraShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllComprasByEstadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where estado not equals to DEFAULT_ESTADO
        defaultCompraShouldNotBeFound("estado.notEquals=" + DEFAULT_ESTADO);

        // Get all the compraList where estado not equals to UPDATED_ESTADO
        defaultCompraShouldBeFound("estado.notEquals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllComprasByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultCompraShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the compraList where estado equals to UPDATED_ESTADO
        defaultCompraShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllComprasByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList where estado is not null
        defaultCompraShouldBeFound("estado.specified=true");

        // Get all the compraList where estado is null
        defaultCompraShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    void getAllComprasByCursoIsEqualToSomething() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);
        Curso curso;
        if (TestUtil.findAll(em, Curso.class).isEmpty()) {
            curso = CursoResourceIT.createEntity(em);
            em.persist(curso);
            em.flush();
        } else {
            curso = TestUtil.findAll(em, Curso.class).get(0);
        }
        em.persist(curso);
        em.flush();
        compra.setCurso(curso);
        compraRepository.saveAndFlush(compra);
        Long cursoId = curso.getId();

        // Get all the compraList where curso equals to cursoId
        defaultCompraShouldBeFound("cursoId.equals=" + cursoId);

        // Get all the compraList where curso equals to (cursoId + 1)
        defaultCompraShouldNotBeFound("cursoId.equals=" + (cursoId + 1));
    }

    @Test
    @Transactional
    void getAllComprasByUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            usuario = UsuarioResourceIT.createEntity(em);
            em.persist(usuario);
            em.flush();
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).get(0);
        }
        em.persist(usuario);
        em.flush();
        compra.setUsuario(usuario);
        compraRepository.saveAndFlush(compra);
        Long usuarioId = usuario.getId();

        // Get all the compraList where usuario equals to usuarioId
        defaultCompraShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the compraList where usuario equals to (usuarioId + 1)
        defaultCompraShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCompraShouldBeFound(String filter) throws Exception {
        restCompraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compra.getId().intValue())))
            .andExpect(jsonPath("$.[*].percentualDesconto").value(hasItem(DEFAULT_PERCENTUAL_DESCONTO.doubleValue())))
            .andExpect(jsonPath("$.[*].valorFinal").value(hasItem(DEFAULT_VALOR_FINAL.doubleValue())))
            .andExpect(jsonPath("$.[*].dataCriacao").value(hasItem(sameInstant(DEFAULT_DATA_CRIACAO))))
            .andExpect(jsonPath("$.[*].formaPagamento").value(hasItem(DEFAULT_FORMA_PAGAMENTO.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));

        // Check, that the count call also returns 1
        restCompraMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCompraShouldNotBeFound(String filter) throws Exception {
        restCompraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCompraMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCompra() throws Exception {
        // Get the compra
        restCompraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCompra() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        int databaseSizeBeforeUpdate = compraRepository.findAll().size();

        // Update the compra
        Compra updatedCompra = compraRepository.findById(compra.getId()).get();
        // Disconnect from session so that the updates on updatedCompra are not directly saved in db
        em.detach(updatedCompra);
        updatedCompra
            .percentualDesconto(UPDATED_PERCENTUAL_DESCONTO)
            .valorFinal(UPDATED_VALOR_FINAL)
            .dataCriacao(UPDATED_DATA_CRIACAO)
            .formaPagamento(UPDATED_FORMA_PAGAMENTO)
            .estado(UPDATED_ESTADO);
        CompraDTO compraDTO = compraMapper.toDto(updatedCompra);

        restCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compraDTO))
            )
            .andExpect(status().isOk());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
        Compra testCompra = compraList.get(compraList.size() - 1);
        assertThat(testCompra.getPercentualDesconto()).isEqualTo(UPDATED_PERCENTUAL_DESCONTO);
        assertThat(testCompra.getValorFinal()).isEqualTo(UPDATED_VALOR_FINAL);
        assertThat(testCompra.getDataCriacao()).isEqualTo(UPDATED_DATA_CRIACAO);
        assertThat(testCompra.getFormaPagamento()).isEqualTo(UPDATED_FORMA_PAGAMENTO);
        assertThat(testCompra.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingCompra() throws Exception {
        int databaseSizeBeforeUpdate = compraRepository.findAll().size();
        compra.setId(count.incrementAndGet());

        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompra() throws Exception {
        int databaseSizeBeforeUpdate = compraRepository.findAll().size();
        compra.setId(count.incrementAndGet());

        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompra() throws Exception {
        int databaseSizeBeforeUpdate = compraRepository.findAll().size();
        compra.setId(count.incrementAndGet());

        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompraWithPatch() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        int databaseSizeBeforeUpdate = compraRepository.findAll().size();

        // Update the compra using partial update
        Compra partialUpdatedCompra = new Compra();
        partialUpdatedCompra.setId(compra.getId());

        partialUpdatedCompra
            .percentualDesconto(UPDATED_PERCENTUAL_DESCONTO)
            .valorFinal(UPDATED_VALOR_FINAL)
            .formaPagamento(UPDATED_FORMA_PAGAMENTO)
            .estado(UPDATED_ESTADO);

        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompra))
            )
            .andExpect(status().isOk());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
        Compra testCompra = compraList.get(compraList.size() - 1);
        assertThat(testCompra.getPercentualDesconto()).isEqualTo(UPDATED_PERCENTUAL_DESCONTO);
        assertThat(testCompra.getValorFinal()).isEqualTo(UPDATED_VALOR_FINAL);
        assertThat(testCompra.getDataCriacao()).isEqualTo(DEFAULT_DATA_CRIACAO);
        assertThat(testCompra.getFormaPagamento()).isEqualTo(UPDATED_FORMA_PAGAMENTO);
        assertThat(testCompra.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdateCompraWithPatch() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        int databaseSizeBeforeUpdate = compraRepository.findAll().size();

        // Update the compra using partial update
        Compra partialUpdatedCompra = new Compra();
        partialUpdatedCompra.setId(compra.getId());

        partialUpdatedCompra
            .percentualDesconto(UPDATED_PERCENTUAL_DESCONTO)
            .valorFinal(UPDATED_VALOR_FINAL)
            .dataCriacao(UPDATED_DATA_CRIACAO)
            .formaPagamento(UPDATED_FORMA_PAGAMENTO)
            .estado(UPDATED_ESTADO);

        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompra))
            )
            .andExpect(status().isOk());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
        Compra testCompra = compraList.get(compraList.size() - 1);
        assertThat(testCompra.getPercentualDesconto()).isEqualTo(UPDATED_PERCENTUAL_DESCONTO);
        assertThat(testCompra.getValorFinal()).isEqualTo(UPDATED_VALOR_FINAL);
        assertThat(testCompra.getDataCriacao()).isEqualTo(UPDATED_DATA_CRIACAO);
        assertThat(testCompra.getFormaPagamento()).isEqualTo(UPDATED_FORMA_PAGAMENTO);
        assertThat(testCompra.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingCompra() throws Exception {
        int databaseSizeBeforeUpdate = compraRepository.findAll().size();
        compra.setId(count.incrementAndGet());

        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compraDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompra() throws Exception {
        int databaseSizeBeforeUpdate = compraRepository.findAll().size();
        compra.setId(count.incrementAndGet());

        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompra() throws Exception {
        int databaseSizeBeforeUpdate = compraRepository.findAll().size();
        compra.setId(count.incrementAndGet());

        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(compraDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompra() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        int databaseSizeBeforeDelete = compraRepository.findAll().size();

        // Delete the compra
        restCompraMockMvc
            .perform(delete(ENTITY_API_URL_ID, compra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
