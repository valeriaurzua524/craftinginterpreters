#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct Node{
    char*value;
    struct Node*prev;
    struct Node*next;
}Node;
typedef struct{
    Node*head;
    Node*tail;
}List;

void initList(List*list){
    list->head=NULL;
    list->tail=NULL;
}
void insert(List*list,const char*str){
    Node*n=malloc(sizeof(Node));
    n->value=malloc(strlen(str)+1);
    strcpy(n->value,str);

    n->prev=list->tail;
    n->next=NULL;
    if(list->tail!=NULL)
        list->tail->next=n;
    else
        list->head=n;

    list->tail=n;
}
Node*find(List*list,const char*str){
    Node*cur=list->head;
    while(cur!=NULL){
        if(strcmp(cur->value,str)==0)
            return cur;
        cur=cur->next;
    }
    return NULL;
}
void deleteValue(List*list,const char*str){
    Node*n=find(list,str);
    if(n==NULL)return;

    if(n->prev!=NULL)
        n->prev->next=n->next;
    else
        list->head=n->next;

    if(n->next!=NULL)
        n->next->prev=n->prev;
    else
        list->tail=n->prev;

    free(n->value);
    free(n);
}
void printList(List*list){
    Node*cur=list->head;
    while(cur!=NULL){
        printf("%s ",cur->value);
        cur=cur->next;
    }
    printf("\n");
}
void freeList(List*list){
    Node*cur=list->head;
    while(cur!=NULL){
        Node*next=cur->next;
        free(cur->value);
        free(cur);
        cur=next;
    }
}

int main(){
    List list;
    initList(&list);

    insert(&list,"valeria");
    insert(&list,"cs4080");
    printList(&list);

    Node*found=find(&list,"cs4080");
    if(found)
        printf("Found:%s\n",found->value);

    deleteValue(&list,"cs4080");
    printList(&list);
    freeList(&list);
    return 0;
}
