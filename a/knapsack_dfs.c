#include <stdio.h>
#include <stdlib.h>
#include "node.h"

int N,W;

int maxprofit = 0;

int main(void){
    scanf("%d %d",&N,&W);
    item item_list[N];
    int item_index[N];
    for(int i = 0;i<N;i++){
        int p , w = 0;
        while(p <= 0 || w <= 0 || p%w > 0){
            scanf("%d %d",&p,&w);
        }
        item_index[i] = 0;
        item_list[i].profit = p;
        item_list[i].weight = w;
        item_list[i].unit_profit = p/w;
    }
    qsort(item_list,N,sizeof(item),compare_unit_profit);
    tree* dfs_tree = create_tree();
    node* root = create_root(item_list,dfs_tree);
    node* current = root;
    while(root->visited == 0){
        if(current->visited == 1){
            if(current->parent->right == current){
                current = current->parent;
                current->visited = 1;
            }
            else{
                current = current->parent;
                if(current->bound > maxprofit){
                    dfs_tree->size++;
                    node* new_node = create_node(item_list,current,0,item_index);
                    current = new_node;
                }
                else{
                    current->visited = 1;
                }
            }
        }
        else if(current->item_no == N-1){
            if(current->parent->left == current){
                current = current->parent;
                dfs_tree->size++;
                node* new_node = create_node(item_list,current,0,item_index);
                current = new_node;
            }
            else{
                current = current->parent;
                current->visited = 1;
            }
        }
        else if(current->bound > maxprofit){
            dfs_tree->size++;
            if(current->weight + item_list[current->item_no + 1].weight <= W){
                node* new_node = create_node(item_list,current,1,item_index);
                current = new_node;
            }
            else{
                dfs_tree->size++;
                node* new_node = create_node(item_list,current,0,item_index);
                current = new_node;
            }
        }
        else if(current->bound <= maxprofit){
            if(current->parent->right == current){
                current = current->parent;
                current->visited = 1;
            }
            else{
                current->parent = current;
            }
        }
    }
    printf("maxprofit : %d\n방문 노드 개수 : %d\n",maxprofit,dfs_tree->size);
    printf("---선택한 아이템 종류---\n");
    for(int i = 0;i<N;i++){
        if(item_index[i]){
            printf("profit : %d, weight : %d\n",item_list[i].profit,item_list[i].weight);
        }
    }
    return 0;                
}

tree* create_tree(){
    tree* temp = (tree*) malloc(sizeof(tree));
    temp->size = 0;
    temp->root = NULL;
    return temp;
}

node* create_root(item item_list[],tree* t){
    node* temp = (node*) malloc(sizeof(node));
    temp->profit = 0;
    temp->weight = 0;
    temp->totweight = 0;
    temp->bound = 0;
    temp->item_no = -1;
    temp->parent = NULL;
    temp->left = NULL;
    temp->right = NULL;
    int i = 0;
    while(i < N){
        if(((temp->totweight) + item_list[i].weight) > W){
            temp->bound += (W - temp->totweight) * item_list[i].unit_profit;
            break;
        }
        temp->totweight += item_list[i].weight;
        temp->bound += item_list[i++].profit;
    }
    t->root = temp;
    t->size++;
    return temp;
}

node* create_node(item item_list[],node* p,int inc,int item_index[]){
    node* temp = (node*) malloc(sizeof(node));
    temp->item_no = p->item_no + 1;    
    if(inc){
        p->left = temp;
        temp->parent = p;
        temp->profit = p->profit + item_list[temp->item_no].profit;
        temp->weight = p->weight + item_list[temp->item_no].weight;
        temp->totweight = p->totweight;
        temp->bound = p-> bound;
    }
    else{
        p->right = temp;
        temp->parent = p;
        temp->profit = p->profit;
        temp->weight = p->weight;
        temp->totweight = temp->weight;
        temp->bound = temp->profit;
        int i = temp->item_no + 1;
        while(i < N){
            if(((temp->totweight) + item_list[i].weight) > W){
                temp->bound += (W - temp->totweight) * item_list[i].unit_profit;
                break;
            }
            temp->totweight += item_list[i].weight;
            temp->bound += item_list[i++].profit;
        }
    }
    if(temp->profit > maxprofit){
        for(int i = 0;i<N;i++){
            item_index[i] = 0;
        }
        node* cnt = temp;
        while(cnt->parent != NULL){
            if(cnt->parent->left == cnt){
                item_index[cnt->item_no] = 1;
            }
            cnt = cnt->parent;
        }
        maxprofit = temp->profit;
    }
    return temp;
}