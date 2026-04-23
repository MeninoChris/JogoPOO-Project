package personagens;

import armas.base.Arma;
import combate.CombateJogador;
import inventario.Inventario;
import itens.Consumivel;
import itens.Fruta;
import itens.PergaminhoCritico;
import itens.PergaminhoForca;
import itens.PocaoCura;
import java.util.List;
import progressao.AtributoEvolutivo;
import progressao.ProgressaoJogador;
import progressao.Talento;

public class Jogador extends Criatura {
    private final Inventario inventario;
    private final ProgressaoJogador progressao;
    private final CombateJogador combate;

    public Jogador(String nome, Arma[] armasEscolhidas) {
        super(nome, 900);
        this.inventario = new Inventario(
            armasEscolhidas,
            new Consumivel[] {
                new Fruta("Maca", 45, 20),
                new Fruta("Coco", 80, 35),
                new PocaoCura(150, 60),
                new PergaminhoForca(25),
                new PergaminhoCritico(20, 0.5)
            }
        );
        this.progressao = new ProgressaoJogador();
        this.combate = new CombateJogador();
    }

    @Override
    public void fazAtaque(Criatura alvo) {
        atacarComArma(0, alvo);
    }

    @Override
    public String getCodinome() {
        return "[JOGADOR " + getNome() + "]";
    }

    public Inventario getInventario() {
        return this.inventario;
    }

    public void atacarComArma(int indiceArma, Criatura alvo) {
        this.combate.atacarComArma(this, this.inventario, indiceArma, alvo);
    }

    public void usarConsumivel(Consumivel consumivel) {
        narrar("usou " + consumivel.getNomeExibicao() + ". Efeito: " + consumivel.getDescricaoPreBatalha() + ".");
        this.inventario.removerConsumivel(consumivel);
        consumivel.usar(this);
    }

    public void prepararDefesa(int indiceArma) {
        this.combate.prepararDefesa(this, this.inventario, this.progressao, indiceArma);
    }

    public boolean podeUsarHabilidadeEspecial() {
        return this.combate.podeUsarHabilidadeEspecial();
    }

    public void usarHabilidadeEspecial(Criatura alvo) {
        this.combate.usarHabilidadeEspecial(this, this.progressao, alvo);
    }

    public String getDescricaoHabilidadeEspecial() {
        return this.combate.getDescricaoHabilidadeEspecial();
    }

    public String getResumoProgressao() {
        return this.progressao.getResumoProgressao(this);
    }

    public String getResumoTalentos() {
        return this.progressao.getResumoTalentos();
    }

    public List<Talento> getTalentosAprendidos() {
        return this.progressao.getTalentosAprendidos();
    }

    public String getResumoBonusArmaPrincipal() {
        return this.combate.getResumoBonusArmaPrincipal();
    }

    public void avancarTurno() {
        this.combate.avancarTurno();
    }

    public void prepararParaNovaBatalha() {
        this.combate.prepararParaNovaBatalha(this);
    }

    @Override
    public void tomaDano(int dano) {
        tomaDano(dano, null);
    }

    @Override
    public void tomaDano(int dano, Criatura atacante) {
        this.combate.processarDanoRecebido(this, this.inventario, this.progressao, dano, atacante);
    }

    public void sofrerDanoDireto(int dano) {
        super.tomaDano(dano);
    }

    public void adicionarBonusDano(int bonusDano) {
        this.progressao.adicionarBonusDano(this, bonusDano);
    }

    public void adicionarBonusCritico(int bonusChanceCritico, double bonusMultiplicadorCritico) {
        this.progressao.adicionarBonusCritico(this, bonusChanceCritico, bonusMultiplicadorCritico);
    }

    @Override
    public void fraseApresentacao() {
        falar("Nao contava com minha astucia!");
    }

    @Override
    public void fraseMorte() {
        falar("Eu vou voltar pra te arrasar!");
    }

    public void aplicarEscudoTemporario(int valor) {
        this.combate.aplicarEscudoTemporario(this, this.progressao, valor);
    }

    public void aplicarRecuperacao(int curaBase, int escudoBase) {
        this.combate.aplicarRecuperacao(this, this.progressao, curaBase, escudoBase);
    }

    public int ganharExperiencia(int experiencia) {
        return this.progressao.ganharExperiencia(this, this.inventario, experiencia);
    }

    public int getNivel() {
        return this.progressao.getNivel();
    }

    public int getPontosAtributoDisponiveis() {
        return this.progressao.getPontosAtributoDisponiveis();
    }

    public int getPontosTalentoDisponiveis() {
        return this.progressao.getPontosTalentoDisponiveis();
    }

    public List<Talento> getTalentosDisponiveis() {
        return this.progressao.getTalentosDisponiveis();
    }

    public int getBonusDanoComArma(Arma arma) {
        return this.combate.getBonusDanoComArma(this.inventario, this.progressao, arma);
    }

    public int getBonusChanceAcertoComArma(Arma arma) {
        return this.combate.getBonusChanceAcertoComArma(this.inventario, arma);
    }

    public int getBonusChanceCriticoComArma(Arma arma) {
        return this.combate.getBonusChanceCriticoComArma(this.inventario, this.progressao, arma);
    }

    public double getBonusMultiplicadorCriticoComArma(Arma arma) {
        return this.combate.getBonusMultiplicadorCriticoComArma(this.progressao);
    }

    public void investirAtributo(AtributoEvolutivo atributo) {
        this.progressao.investirAtributo(this, atributo);
    }

    public void aprenderTalento(Talento talento) {
        this.progressao.aprenderTalento(this, talento);
    }
}
