package sortbench.Model;

    public class DataSet {
        
        private int[] dados;
        
        public DataSet(int[] dados){
            this.dados = dados.clone();
        }
        
        public int[] getDados(){
            return dados.clone();
        }
        
        public int getTamanho(){
            return dados.length;
        }
}
