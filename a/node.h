typedef struct item{
    int profit;
    int weight;
    int unit_profit;
}item;

typedef struct node{
    int profit;
    int weight;
    int totweight;
    int bound;
    int item_no;
    int visited;
    struct node* parent;
    struct node* left;
    struct node* right;
}node;

typedef struct tree{
    int size;
    struct node* root;
}tree;

int compare_unit_profit(const void *v1, const void *v2){
    const item *m1 = (const item *) v1;
    const item *m2 = (const item *) v2;
    return -(m1->unit_profit - m2->unit_profit);
}

tree* create_tree();
node* create_root(item[],tree*);
node* create_node(item[],node*,int,int[]);




