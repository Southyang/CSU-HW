#include <stdio.h>
#include <stdlib.h>
#include <conio.h>
#include <string.h>
#define N 20//�������Ϣ��20�ַ�����
#define MAX 100//��¼һ�ٱ������һ�ٸ�ѧ��������� 
FILE *fp1,*fp2,*fp3;
int num=0,num1=0;
int i,j;            
int temp=0;

void show();//��ʾ����˵� 
void add_book();//������� 
void find_book();//��ѯͼ��״̬ 
void borrow_book();//����ͼ�飨ɾ����ͼ�鲿����Ϣ�� 
void return_book();//���飨�����鼮��Ϣ�� 
void record_book();//��ѯѧ���Ľ����¼ 
void import();//�������� 
void old_add();//ԭ���鼮��� 
void huanshu();//��ѯѧ���Ļ������ 
typedef struct book//�����鼮��Ϣ�ṹ��
{
	char b_name[N];
	char b_number[N];
	char b_price[N];
	char b_writer[N];
	int  b_quant;
}book; 
book b_list[MAX];//�鼮��Ϣ�б� 

typedef struct reader//����ѧ��������Ϣ�ṹ��
{
	char r_name[N];
	char r_number[N];
	char r_books[N];
}reader; 
reader r_list[MAX];//��������б� 

typedef struct ret//����ѧ��������Ϣ�ṹ��
{
	char re_name[N];
	char re_number[N];
	char re_books[N];
	char re_huan[N];
}ret; 
ret re_list[MAX];//��������б� 

void import(char information[1000],char *p[])//������Ϣ���� 
{
	printf("\n\t\t %s:",information);
	fflush(stdin);
	gets(*p);
}

void show()//����˵� 
{
	char order0,order;
	                                                                           
L0:	printf("\n\n\n\n\n\t\t* * * * * * * * * * * * * * * * * * *\n");           
    printf("\t\t*          ͼ�����ϵͳ             *");                     printf("\t* * * * * * * * * * * * * * * * * * * * * *\n");
    printf("\t\t*                                   *");                     printf("\t*        ��ӭʹ��ͼ��ݹ���ϵͳ           *\n"); 
	printf("\t\t*    ����������ѡ������Ҫ�ķ���     *");                     printf("\t*����һ�����飬����һ�����ѡ�����갿˼� *\n");
	printf("\t\t*     1.�������                    *");                     printf("\t*                                         *\n");
	printf("\t\t*     2.ͼ��״̬��ѯ                *");                     printf("\t*ϣ�������߿�����ͼ��������ܶ���Ŀ��� *\n");
	printf("\t\t*     3.���飨ÿ�����ֻ�ܽ��屾�飩*");                     printf("\t* * * * * * * * * * * * * * * * * * * * * *\n");
	printf("\t\t*     4.����                        *\n");
	printf("\t\t*     5.���������ѯ                *\n");
	printf("\t\t*     6.���������ѯ                *\n");
	printf("\t\t*     7.ԭ���鼮���                *\n");
	printf("\t\t*     8.��������ʹ��                *\n");
	printf("\t\t* * * * * * * * * * * * * * * * * * *\n"); 
    printf("\t\t   ��ѡ������Ҫ�Ĺ��ܣ�");

	order=getchar();
	
	  switch(order)
	  {
		case '1':add_book();   break;
		case '2':find_book();  break;
		case '3':borrow_book();break;
		case '4':return_book();break;
		case '5':record_book();break;
		case '6':huanshu();    break;
		case '7':old_add();    break;
		case '8':goto L;
		default :printf("\n\t\t\tWronging order\n");
	  }
    getchar();
    printf("�Ƿ����ʹ��ͼ�����ϵͳ��(��y / ��n)");
	order0=getchar();
	getchar();
	if(order0=='y'||order0=='Y')  
	{
		system("cls");goto L0;
	}
	
	L:
	system("cls");
	printf("\n\n\n\n\t\t\t\t��лʹ��ͼ�����ϵͳ\n");
}

void add_book()//������� 
{
    char order1;
    int M,A=0;
    int figure;
    L1:
    fp1=fopen("tushushujiku.txt","a+");
	for(i=0;i<100;i++)                        //���ĵ��ڵ����ݷ���ṹ���� 
	{
		fscanf(fp1,"%s",&b_list[i].b_name);
		fscanf(fp1,"%s",&b_list[i].b_number);
		fscanf(fp1,"%s",&b_list[i].b_price); 
		fscanf(fp1,"%s",&b_list[i].b_writer);
		fscanf(fp1,"%d",&b_list[i].b_quant);
		if(b_list[i].b_name[0]!=0)
		M++;
	}
	fclose(fp1);
	
	printf("��������ͼ������Ϊ��");
	scanf("%d",&figure);
	for(i=M;i<M+figure;i++)
    {
	printf("�� %d ���鼮��Ϣ��\n",i+1);
	import("�鼮����",b_list[i].b_name);//�����鼮��Ϣ����ָ�뱣��
	import("�鼮���",b_list[i].b_number);
	import("�鼮�۸�",b_list[i].b_price);
	import("�鼮����",b_list[i].b_writer);
	printf("\n\t\t �鼮����:");
	scanf("%d",&b_list[i].b_quant);
	A=i+1;
    }
    
    fp1=fopen("tushushujiku.txt","a+");
    for(i=M;i<A;i++)
    {
	fprintf(fp1,"%s\t",b_list[i].b_name);//��ָ�������ݴ���txt�ĵ� 
	fprintf(fp1,"%s\t",b_list[i].b_number);
	fprintf(fp1,"%s\t",b_list[i].b_price);
	fprintf(fp1,"%s\t",b_list[i].b_writer);
	fprintf(fp1,"%d\t",b_list[i].b_quant);
	fprintf(fp1,"\n");
	}
	fclose(fp1);
	
	printf("¼��ɹ�\n");
	getchar();
	printf("�Ƿ����¼�룿(��y / ��n)");
	order1=getchar();
	if(order1=='y'||order1=='Y')
	goto L1;
	
    
}

void find_book()//ͼ��״̬��ѯ 
{
	char order2;
	
	char book_name[100];

	
	L2:
	fp1=fopen("tushushujiku.txt","a+");
	
	import("������Ҫ��ѯ���鼮",book_name); 
	for(i=0;i<100;i++)                            //���ĵ�������ȫ������ṹ������ 
	{
		fscanf(fp1,"%s",&b_list[i].b_name);
		fscanf(fp1,"%s",&b_list[i].b_number);
		fscanf(fp1,"%s",&b_list[i].b_price); 
		fscanf(fp1,"%s",&b_list[i].b_writer);
		fscanf(fp1,"%d",&b_list[i].b_quant);
	}
	for(i=0;i<100;i++)
	{
	    if(!strcmp(b_list[i].b_name,book_name))   //�ԱȽṹ�����ڵ�������ú������������ 
		{
	    	temp=i;
		}
	}
	printf("�鼮���� �鼮��� �鼮�۸� �鼮���� �鼮����\n");
	if(!strcmp(b_list[temp].b_name,book_name))    //�����ѯ���Ľ�� 
	{
		printf("%s\t  ",b_list[temp].b_name);
		printf("%s\t  ",b_list[temp].b_number);
		printf("%s\t  ",b_list[temp].b_price);
		printf("%s\t  ",b_list[temp].b_writer);
		printf("%d\t  ",b_list[temp].b_quant);
	}
	else printf("\n��ѯ�޽��\n");
	getchar();
	fclose(fp1);
	printf("\n�Ƿ������ѯ��(��y / ��n)");
	order2=getchar();
	if(order2=='y'||order2=='Y')
	goto L2;
}

void borrow_book()//����ͼ��
{
	char order3;
	int M=0;
	char book_name[100];
	L3:
	fp1=fopen("tushushujiku.txt","a+");
	
	for(i=0;i<100;i++)                        //���ĵ��ڵ����ݷ���ṹ���� 
	{
		fscanf(fp1,"%s",&b_list[i].b_name);
		fscanf(fp1,"%s",&b_list[i].b_number);
		fscanf(fp1,"%s",&b_list[i].b_price); 
		fscanf(fp1,"%s",&b_list[i].b_writer);
		fscanf(fp1,"%d",&b_list[i].b_quant);
		if(b_list[i].b_name[0]!=0)
		M++;
	}
	fclose(fp1);
    import("������Ҫ���ĵ��鼮",book_name); 
    
    for(i=0;i<100;i++)                                //�ԱȽṹ�����ڵ����ݺ͸ú������������� 
	{
	    if(!strcmp(b_list[i].b_name,book_name))
		{
	    	temp=i;
		}
	}
	if(!strcmp(b_list[temp].b_name,book_name))          //����ѧ����Ϣ���������ѯ��� 
	{
		if(b_list[temp].b_quant>0)
		{
		   import("ѧ������",r_list[0].r_name);
		   import("ѧ��ѧ��",r_list[0].r_number);
		   import("�鼮����",r_list[0].r_books);
	       b_list[temp].b_quant=b_list[temp].b_quant-1;
	       printf("����ɹ�\n");
		}
		else printf("�鼮����Ϊ��\n");
	}
	else
	printf("��ѯ�޽��\n");
	
	fp1=fopen("tushushujiku.txt","w");
	for(i=0;i<M;i++)                                   //���µ����ݴӽṹ��������ļ��� 
	{
	fprintf(fp1,"%s\t",b_list[i].b_name);
	fprintf(fp1,"%s\t",b_list[i].b_number);
	fprintf(fp1,"%s\t",b_list[i].b_price);
	fprintf(fp1,"%s\t",b_list[i].b_writer);
	fprintf(fp1,"%d\t",b_list[i].b_quant);
	fprintf(fp1,"\n");
    }
    fclose(fp1);
    
    fp2=fopen("jieshuqingkuang.txt","a+");                //��ѧ��������������ļ�2 
	
	fprintf(fp2,"%s\t",r_list[0].r_name);
	fprintf(fp2,"%s\t",r_list[0].r_number);
	fprintf(fp2,"%s\t",r_list[0].r_books);
	fprintf(fp2,"\n");
	
	fclose(fp2);
	getchar();
    printf("�Ƿ�������ģ�(��y / ��n)");
	order3=getchar();
	if(order3=='y'||order3=='Y')
	goto L3;
}

void return_book()//���� 
{
	char order4;
	char book_name[100];
	char stu_name[100];
	int M=0;
	L4:
	fp1=fopen("tushushujiku.txt","a+");
	
	for(i=0;i<100;i++)                            //���ļ������ݷ���ṹ���� 
	{
		fscanf(fp1,"%s",&b_list[i].b_name);
		fscanf(fp1,"%s",&b_list[i].b_number);
		fscanf(fp1,"%s",&b_list[i].b_price);
		fscanf(fp1,"%s",&b_list[i].b_writer);
		fscanf(fp1,"%d",&b_list[i].b_quant);
		if(b_list[i].b_name[0]!=0)
		M++;
	}
	fclose(fp1);
	
	import("������Ҫ���ص��鼮",book_name);
	import("������ѧ������",stu_name);
	
	for(i=0;i<M;i++)                                  //�Աȡ���ѯ 
	{
	    if(!strcmp(b_list[i].b_name,book_name))
		{
	    	temp=i;
		}
	}
	
	if(!strcmp(b_list[temp].b_name,book_name))
	{
		b_list[temp].b_quant=b_list[temp].b_quant+1;
	}
	else printf("���޴���\n");
	
	fp1=fopen("tushushujiku.txt","w");
	for(i=0;i<M;i++)                                   //���µ����ݴӽṹ��������ļ��� 
	{
	fprintf(fp1,"%s\t",b_list[i].b_name);
	fprintf(fp1,"%s\t",b_list[i].b_number);
	fprintf(fp1,"%s\t",b_list[i].b_price);
	fprintf(fp1,"%s\t",b_list[i].b_writer);
	fprintf(fp1,"%d\t",b_list[i].b_quant);
	fprintf(fp1,"\n");
    }
    fclose(fp1);
	
	fp2=fopen("jieshuqingkuang.txt","a+");
	for(j=0;j<100;j++)                                       //���ݴ� �ļ� ���ṹ���� 
	{
		fscanf(fp2,"%s",&r_list[j].r_name);
		fscanf(fp2,"%s",&r_list[j].r_number);
		fscanf(fp2,"%s",&r_list[j].r_books);
	}
	fclose(fp2);
	
	for(j=0;j<100;j++)                                //�Աȡ���ѯ 
	{
	    if(strcmp(r_list[j].r_books,book_name)==0&&strcmp(r_list[j].r_name,stu_name)==0)
		{
	    	temp=j;
		}
	}
	
	fp3=fopen("huanshuqingkuang.txt","a+");
	if(strcmp(r_list[temp].r_books,book_name)==0&&strcmp(r_list[temp].r_name,stu_name)==0)            //�����ѯ��� 
		{
		fprintf(fp2,"%s\t%s\t%s\t�ѻ�\n",r_list[temp].r_name,r_list[temp].r_number,r_list[temp].r_books);//���������¼���ļ�3 
		printf("����ɹ�\n");
		}
	else printf("�޽��ļ�¼\n");
    fclose(fp3);
    
    getchar();
    printf("�Ƿ�������飿(��y / ��n)");
	order4=getchar();
	if(order4=='y'||order4=='Y')
    goto L4;
}

void record_book()//��ѯѧ�������¼
{ 
    i=0;
    int a[100];
    int order5;
    char s_name[100];
    
	fp2=fopen("jieshuqingkuang.txt","a+");
	for(j=0;j<100;j++)                                          //�ļ� ���ṹ���� 
	{
		fscanf(fp2,"%s",&r_list[j].r_name);
		fscanf(fp2,"%s",&r_list[j].r_number);
		fscanf(fp2,"%s",&r_list[j].r_books);
	}
    L5:
	import("������ѧ������",s_name);
	
	for(j=0;j<100;j++)                                            //�Աȡ���ѯ 
	{
		if(!strcmp(r_list[j].r_name,s_name))
		{
			a[i]=j;
			i++;
		}
	}
    printf("\t\t\t\tѧ������ ѧ��ѧ�� ��������\n");
    for(j=0;j<i;j++)
    if(!strcmp(r_list[a[j]].r_name,s_name))
    printf("\t\t\t\t%s\t%s\t%s\n",r_list[a[j]].r_name,r_list[a[j]].r_number,r_list[a[j]].r_books);
	fclose(fp2);
	
    getchar();
	printf("�Ƿ������ѯ��(��y / ��n)");
	order5=getchar();
	if(order5=='y'||order5=='Y')
	goto L5;
}  

void huanshu()//��ѯѧ�������¼ 
{
	i=0;
	int a[100];
    int order6;
    char s_name[100];
    
	fp3=fopen("huanshuqingkuang.txt","a+");
	for(j=0;j<100;j++)                                          //�ļ� ���ṹ���� 
	{
		fscanf(fp3,"%s",&re_list[j].re_name);
		fscanf(fp3,"%s",&re_list[j].re_number);
		fscanf(fp3,"%s",&re_list[j].re_books);
		fscanf(fp3,"%s",&re_list[j].re_huan);
	}
    L6:
	import("������ѧ������",s_name);
	
	for(j=0;j<100;j++)                                            //�Աȡ���ѯ 
	{
		if(!strcmp(re_list[j].re_name,s_name))
		{
			a[i]=j;
			i++;
		}
	}
    printf("\n\t\t\t\tѧ������ ѧ��ѧ�� ��������\n");
    for(j=0;j<i;j++)
	if(!strcmp(re_list[a[j]].re_name,s_name))
    printf("\t\t\t\t%s\t%s\t%s\t%s\n",re_list[a[j]].re_name,re_list[a[j]].re_number,re_list[a[j]].re_books,re_list[a[j]].re_huan);
	fclose(fp3);
	
    getchar();
	printf("�Ƿ������ѯ��(��y / ��n)");
	order6=getchar();
	if(order6=='y'||order6=='Y')
	goto L6;
}

void old_add()//ԭ���鼮��� 
{
	char order7;
	int M=0;
	char book_name[100];
	int number;
	L7:
	fp1=fopen("tushushujiku.txt","a+");
	
	for(i=0;i<100;i++)                        //���ĵ��ڵ����ݷ���ṹ���� 
	{
		fscanf(fp1,"%s",&b_list[i].b_name);
		fscanf(fp1,"%s",&b_list[i].b_number);
		fscanf(fp1,"%s",&b_list[i].b_price); 
		fscanf(fp1,"%s",&b_list[i].b_writer);
		fscanf(fp1,"%d",&b_list[i].b_quant);
		if(b_list[i].b_name[0]!=0)
		M++;
	}
	fclose(fp1);
    
	import("������Ҫ�����鼮",book_name); 
    printf("\t\t������Ҫ���ӵ�������");
	scanf("%d",&number); 
    
    for(i=0;i<100;i++)                                //�ԱȽṹ�����ڵ����ݺ͸ú������������� 
	{
	    if(!strcmp(b_list[i].b_name,book_name))
		{
	    	temp=i;
		}
	}
	if(!strcmp(b_list[temp].b_name,book_name))          //�����鼮��Ϣ���������ѯ��� 
	{
		b_list[temp].b_quant=b_list[temp].b_quant+number;
		printf("���ɹ�\n");
	}
	else printf("\t\t���޴���\n");
	
	fp1=fopen("tushushujiku.txt","w");
	for(i=0;i<M;i++)                                   //���µ����ݴӽṹ��������ļ��� 
	{
	fprintf(fp1,"%s\t",b_list[i].b_name);
	fprintf(fp1,"%s\t",b_list[i].b_number);
	fprintf(fp1,"%s\t",b_list[i].b_price);
	fprintf(fp1,"%s\t",b_list[i].b_writer);
	fprintf(fp1,"%d\t",b_list[i].b_quant);
	fprintf(fp1,"\n");
    }
    fclose(fp1);
    
    getchar();
    printf("�Ƿ����¼���鼮��(��y / ��n)");
	order7=getchar();
	if(order7=='y'||order7=='Y')
	goto L7;
}

int main()//������ 
{
	system("color f1");//������ɫ
	show();
	
	return 0;
}
