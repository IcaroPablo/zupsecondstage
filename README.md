# Como escrever uma simples API RESTful usando Spring + Hibernate (ou morrer tentando)

Olá meu nobre rapaz, sim, você mesmo, fiquei sabendo que você está querendo entrar na máfia, ops... aprender a escrever uma API em Java usando Spring (*cof cof*). Bom, não importa, eu posso ensinar as duas coisas e elas funcionam de maneira bastante parecida na verdade. Então se você estiver interessado em segredos que podem custar sua vida, continue a leitura.

![image](https://user-images.githubusercontent.com/27890590/115122741-e3727180-9f8f-11eb-8d3f-19fb6135c0ec.png)

O esquema dessa vez é bem simples, você está com sorte: precisamos fazer cadastros de usuários e fazer cadastros de endereços desses usuários. Mas, claro, tomando cuidado com armazenamento certo das informações e usando dos recursos disponíveis para avisar qualquer desvio do protocolo padrão (eu sei que você ja escreveu a data no lugar do nome durante a prova na escola, não adianta negar).

Então eu apresento a solução perfeita pra esse problema, a ***Famiglia Corleone***, oh, desculpe, o ***SPRING MVC***.
Então vamos ao funcionamento do nosso código.

# Funcionamento do código

Acompanhe meu raciocínio, o Spring funcionaria perfeitamente como um prédio da máfia, sim sim, acredite em mim, teríamos 3 caixas de correio na entrada do prédio: duas delas seriam para entradas de informação e uma delas seria para a saída. Bom, e para simular o envio de mensages (que daqui pra frente chamaremos de requisições) suponhamos que teríamos um amigável carteiro hipotético (usaremos a ferramenta Postman, ela ilustrará alguns exemplos mais a diante).

Mas onde nós iremos guardar os cadastros feitos para que eles possam ser consultados posteriormente ? Usaremos uma API chamada JPA e um banco de dados baseado em SQL: PostgreSQL. A JPA foi escolhida porque ela é bastante prática. Ela ficará responsável pelo ORM no nosso código (Object-Relational Mapping), ou seja, podemos escrever classes no nosso código que serão interpretadas pela JPA como entidades no banco de dados. Isso facilita muito o trabalho, pois não precisamos ter dores de cabeça com _queries_ em SQL.

>_Pense no JPA como o responsável pela sala de arquivos, ele é um rapaz bem simpático e está sempre disposto a ajudar. Hoje ele tá particularmente animado porque finalmente apareceu uma utilidade pro novo armário dele, um armário onde as gavetas têm mais gavetas dentro, ele foi produzido por uma marca muito boa, a PostgreSQL._

Usaremos a IDE Eclipse para escrever o código pois ela já organiza muito bem a estrutura de arquivos em uma aplicação java e tem vários módulos e funcionalidades embutidos que nos permitem ser mais ágeis na escrita.

Certo, certo, garoto, eu sei que você quer saber o que acontece dentro do prédio... então por onde nós começaremos a fazer isso tudo ?

### CRIANDO O PRÉDIO ORA BOLAS

Podemos iniciar um novo projeto spring pelo site start.spring.io, nesse site selecionaremos nossas especificações, as mais importantes são:

* Java (versão 8) como linguagem de programação
* Maven como gerenciador de dependências
* As dependências são:
    * Spring Web
    * Spring Data JPA
    * PostgreSQL driver
    * Hibernate validation

Quanto a gerenciamento de dependências, o maven é uma ótima ferramenta, pois, com ele, precisamos apenas declarar as dependências que queremos num único arquivo .xml e ele ficará responsável pelo resto: baixar todas as bibliotecas que precisamos e também as bibliotecas que essas bibliotecas precisam.

>_Pense que todos dentro do nosso prédio mafioso se comunicam em uma linguagem secreta chamada java e pense no Maven como se fosse um daqueles contatos esquisitos que todo mafioso tem, ele manda funcionários especiais pra gente de acordo com nossas demandas, esses funcionários são as dependências._

Bom, depois disso importamos o projeto no Eclipse e configuramos o arquivo application.properties para que a relação entre o JPA e o PostgreSQL esteja nos conformes.

>_Sim, o nosso garoto JPA, está lendo o manual do armário novo que ele comprou._

### AGORA SIM, VAMOS AOS DEPARTAMENTOS DO PRÉDIO E SUAS FUNÇÕES

Nossa aplicação terá 5 pacotes principais, um para cada categoria de classes a seguir:
* Models
* Controllers
* Services
* Repositories
* Exception handlers

## O departamento de Model

Escreveremos três classes de model, uma para o cadastro de usuários e outra para o cadastro de endereço dos usuários (e a terceira eu explicarei mais a diante). Nessas classes escreveremos anotações que nos ajudarão com validações nas informações dos cadastros, com destaques para as anotações de email e cpf que exigem que eles não se repitam dentro do banco de dados, essas anotações são do Bean Validation e do Hibernate, eles facilitam o trabalho de verificar se os dados recebidos fazem algum sentido (é como diz aquele ditado, o usuário escreve torto por linhas tortas).

Também temos anotações do JPA:
* Para criar uma entidade e sua respectiva tabela no banco de dados:
    * @Entity e @Table
* Para geração de ids automáticos dentro da tabela, para que não tenhamos que nos preocupar com a ordem do armazenamento de informação
    * @Id e @GeneratedValue(strategy = GenerationType.IDENTITY)
* Para colunas com as informações desejadas na tabela e suas respectivas exigências
    * @Column

>_As classes de model são como envelopes contendo a informação que nós queremos armazenar no arquivo, elas serão entregues ao nosso querido JPA e ele vai guardar elas corretamente seguindo orientações de notas especiais. Essas notas foram escritas pelo Hibernate, um dos capangas do Maven, quer saber como ele sabe disso tudo? Digamos que ele tem os próprios _métodos_ se é que você me entende._

~~~Java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(name = "username", nullable = false)
    private String username;
    
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    
    @Column(name = "cpf", unique = true, nullable = false)
    private String cpf;
    
    @Column(name = "birthday", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
} 
~~~
*Snippet da classe model de usuário*


&nbsp;
~~~Java
@Entity
@Table(name = "addresses")
public class Address {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
	@Column(name = "logradouro", nullable = false)
    private String logradouro;
	
	@Column(name = "numero", nullable = false)
    private String numero;
	
	@Column(name = "complemento", nullable = false)
    private String complemento;
	
	@Column(name = "bairro", nullable = false)
    private String bairro;
	
	@Column(name = "cidade", nullable = false)
    private String cidade;
	
	@Column(name = "estado", nullable = false)
    private String estado;
	
	@Column(name = "cep", nullable = false)
    private String cep;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
~~~
*Snippet da classe model de endereço*


&nbsp;

No noso model de endereços temos uma coluna especial (@JoinColumn(name = "user_id", nullable = false)), essa coluna vai ser responsável por relacionar cada endereço ao seu respectivo usuário numa relação do tipo Many to One, pois um usuário pode ter vários endereços.

>_Deu pra entender por que as gavetas dentro de gavetas são importantes agora né ?_

## O departamento de Controller

No pacote de controller, escrevemos uma classe para ser nosso controller, nessa classe temos uma anotação do Spring (@RestController) que define ela como um controller do Spring, ou seja, uma classe que recebe e direciona requisições para os métodos certos. As anotações de mapping (@RequestMapping, @GetMapping e @PostMapping) servem para especificar onde e o que será feito nos endpoints utilizados na aplicação, no nosso caso, são 3: dois de *POST*, um para cada tipo de cadastro e um de *GET* para obter informações de um usuário já cadastrado).

~~~Java
@RestController
@RequestMapping("/api")
public class UserController {
	
	@Autowired
	private UserService service;
    
    @GetMapping("/users/{id}")
    public ResponseEntity<UserAndAddresses> retrieveUserData(@PathVariable(value = "id") Long userId)
    		throws ResourceNotFoundException {
        UserAndAddresses userdata = service.getUserInformationById(userId);

        return ResponseEntity.ok().body(userdata);
    }
    
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User createNewUser(@Validated @RequestBody User user, BindingResult br)
    		throws DataIntegrityViolationException, Exception{
    	if (br.hasErrors())
    		throw new ConstraintException(br.getAllErrors().get(0).getDefaultMessage());
    	return this.service.createUser(user);
    }
    
    @PostMapping("/users/address/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Address createAddress(@PathVariable(value = "id") Long userId, @Validated @RequestBody Address address, BindingResult br)
    		throws ResourceNotFoundException, DataIntegrityViolationException, Exception{
    	if (br.hasErrors())
    		throw new ConstraintException(br.getAllErrors().get(0).getDefaultMessage());
    	return this.service.createAddress(userId, address);
    }
}
~~~
*Snippet da classe model de endereço*


&nbsp;

Vale notar que no método de *POST* para cadastrar endereço (createAddress), recebemos o id do usuário pela URL, isso serve para que relacionemos o cadastro de endereço ao usuário correto

>_Esses endpoints são as caixas de correio que eu mencionei anteriormente e o controller é como o padrinho da máfia, o manda chuva pra quem todo mundo pede favor, ele já tem um pessoal pronto e selecionado pra cada favor que pedirem pra ele, ou, no caso, para cada tipo de mensagem que chegar pelas caixas de correio._

![image](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSJIs8RsK0H1692Q4BGUVU9RcE88d4KuJ2-LA&usqp=CAU)

Também temos uma outra anotação importante nessa classe, a anotação @autowired, responsável por um dos principais benefícios do spring: a injeção de dependências. Precisamos dessa anotação para poder usar no nosso controler os métodos de um objeto do tipo service (injetando-a na classe de controller), perceba também que o objeto do tipo service foi criado, mas não foi iniciado, o spring iniciará ele quando for necessário.

>_Porque de vez em quando entra gente nova na tarefa, mas é gente de confiança, claro_

## O departamento de Service

Esse departamento é muito próximo dos outros dois departamentos restantes, o de Repositories e o de Exception handling. Nesse pacote, temos a classe de service, que contém métodos fundamentais para o funcionamento da aplicação.

~~~Java
@Service
public class UserService {
	
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AddressRepository addressRepository;
	
    public UserAndAddresses getUserInformationById(Long userId) throws ResourceNotFoundException {
    	User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("There is no User for the id " + userId));
        List<Address> addresses = addressRepository.findByUserId(userId);
        UserAndAddresses userdata = new UserAndAddresses(user, addresses);
        
        return userdata;
    }
    
    public User createUser(User user) throws DataIntegrityViolationException, Exception{
    	try {
    		return userRepository.save(user);
    	}
    	catch(DataIntegrityViolationException e){
    		throw new ConstraintException("Constraint Problem - " + e.getMostSpecificCause().getMessage());
    	}
    	catch(Exception e) {
    		throw new Exception("Unknown error :( but i know some details that could help: " + e.getMessage());
    	}
    }
    
    public Address createAddress(Long userId, Address address) throws ResourceNotFoundException, DataIntegrityViolationException, Exception{
    	User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("There is no User for this id: " + userId));
    	address.setUser(user);
    	
    	try {
    		return addressRepository.save(address);
    	}
    	catch(DataIntegrityViolationException e){
    		throw new ConstraintException("Constraint Problem - " + e.getMostSpecificCause().getMessage());
    	}
    	catch(Exception e) {
    		throw new Exception("Unknown error :( but i know some details that could help: " + e.getMessage());
    	}
    }
}
~~~
*Snippet da classe de service*


&nbsp;

Logo de cara dá pra perceber mais dois @Autowired ali, injetamos objetos das duas classes de repository, essas classes estendem a classe JpaRepository e herdam métodos úteis para que possamos usar nosso banco de dados para salvar as informações desejadas

>_O rapaz JPA trabalha no departamento de repository, ele tem os seus pupilos, o UserRepository e o AddressRepository, ele ensinou muito bem pra eles como se usa a sala do arquivo._

Temos métodos para registrar os usuários, para registrar os endereços de um usuário e para exibir um usuário com todos os seus endereços. Caso algo dê errado no processo, teremos uma noção de onde pode ter ocorrido o erro devido às classes de exception handling que serão explicadas a seguir.

>_O pessoal do departamento de exception handling é bem eficiente, eles têm uns gestos esquisitos pra se comunicar, mas tudo bem_

![imagem](https://thumbs.dreamstime.com/z/homem-africano-que-faz-caretas-da-raiva-guardando-o-dedo-perto-do-pesco%C3%A7o-imitando-faca-cortando-garganta-130221996.jpg)

*Típico funcionário do departamento de exception handling num dia de trabalho normal*


&nbsp;
