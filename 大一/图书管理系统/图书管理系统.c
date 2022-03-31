#include <stdio.h>
#include <stdlib.h>
#include <conio.h>
#include <string.h>
#define N 20//输入的信息在20字符以内
#define MAX 100//记录一百本书或者一百个学生借书情况 
FILE *fp1,*fp2,*fp3;
int num=0,num1=0;
int i,j;            
int temp=0;

void show();//显示界面菜单 
void add_book();//新书入库 
void find_book();//查询图书状态 
void borrow_book();//借阅图书（删除该图书部分信息） 
void return_book();//还书（增加书籍信息） 
void record_book();//查询学生的借书记录 
void import();//输入数据 
void old_add();//原有书籍入库 
void huanshu();//查询学生的还书情况 
typedef struct book//建立书籍信息结构体
{
	char b_name[N];
	char b_number[N];
	char b_price[N];
	char b_writer[N];
	int  b_quant;
}book; 
book b_list[MAX];//书籍信息列表 

typedef struct reader//建立学生借阅信息结构体
{
	char r_name[N];
	char r_number[N];
	char r_books[N];
}reader; 
reader r_list[MAX];//借书情况列表 

typedef struct ret//建立学生还书信息结构体
{
	char re_name[N];
	char re_number[N];
	char re_books[N];
	char re_huan[N];
}ret; 
ret re_list[MAX];//还书情况列表 

void import(char information[1000],char *p[])//输入信息函数 
{
	printf("\n\t\t %s:",information);
	fflush(stdin);
	gets(*p);
}

void show()//界面菜单 
{
	char order0,order;
	                                                                           
L0:	printf("\n\n\n\n\n\t\t* * * * * * * * * * * * * * * * * * *\n");           
    printf("\t\t*          图书管理系统             *");                     printf("\t* * * * * * * * * * * * * * * * * * * * * *\n");
    printf("\t\t*                                   *");                     printf("\t*        欢迎使用图书馆管理系统           *\n"); 
	printf("\t\t*    请输入数字选择所需要的服务     *");                     printf("\t*读过一本好书，像交了一个益友。——臧克家 *\n");
	printf("\t\t*     1.新书入库                    *");                     printf("\t*                                         *\n");
	printf("\t\t*     2.图书状态查询                *");                     printf("\t*希望广大读者可以在图书馆内享受读书的快乐 *\n");
	printf("\t\t*     3.借书（每人最多只能借五本书）*");                     printf("\t* * * * * * * * * * * * * * * * * * * * * *\n");
	printf("\t\t*     4.还书                        *\n");
	printf("\t\t*     5.借书情况查询                *\n");
	printf("\t\t*     6.还书情况查询                *\n");
	printf("\t\t*     7.原有书籍入库                *\n");
	printf("\t\t*     8.结束本次使用                *\n");
	printf("\t\t* * * * * * * * * * * * * * * * * * *\n"); 
    printf("\t\t   请选择您需要的功能：");

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
    printf("是否继续使用图书管理系统？(是y / 否n)");
	order0=getchar();
	getchar();
	if(order0=='y'||order0=='Y')  
	{
		system("cls");goto L0;
	}
	
	L:
	system("cls");
	printf("\n\n\n\n\t\t\t\t感谢使用图书管理系统\n");
}

void add_book()//新书入库 
{
    char order1;
    int M,A=0;
    int figure;
    L1:
    fp1=fopen("tushushujiku.txt","a+");
	for(i=0;i<100;i++)                        //将文档内的数据放入结构数组 
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
	
	printf("本次入库的图书数量为：");
	scanf("%d",&figure);
	for(i=M;i<M+figure;i++)
    {
	printf("第 %d 本书籍信息：\n",i+1);
	import("书籍名称",b_list[i].b_name);//输入书籍信息，用指针保存
	import("书籍编号",b_list[i].b_number);
	import("书籍价格",b_list[i].b_price);
	import("书籍作者",b_list[i].b_writer);
	printf("\n\t\t 书籍余量:");
	scanf("%d",&b_list[i].b_quant);
	A=i+1;
    }
    
    fp1=fopen("tushushujiku.txt","a+");
    for(i=M;i<A;i++)
    {
	fprintf(fp1,"%s\t",b_list[i].b_name);//将指针内数据存入txt文档 
	fprintf(fp1,"%s\t",b_list[i].b_number);
	fprintf(fp1,"%s\t",b_list[i].b_price);
	fprintf(fp1,"%s\t",b_list[i].b_writer);
	fprintf(fp1,"%d\t",b_list[i].b_quant);
	fprintf(fp1,"\n");
	}
	fclose(fp1);
	
	printf("录入成功\n");
	getchar();
	printf("是否继续录入？(是y / 否n)");
	order1=getchar();
	if(order1=='y'||order1=='Y')
	goto L1;
	
    
}

void find_book()//图书状态查询 
{
	char order2;
	
	char book_name[100];

	
	L2:
	fp1=fopen("tushushujiku.txt","a+");
	
	import("请输入要查询的书籍",book_name); 
	for(i=0;i<100;i++)                            //将文档内数据全部放入结构数组中 
	{
		fscanf(fp1,"%s",&b_list[i].b_name);
		fscanf(fp1,"%s",&b_list[i].b_number);
		fscanf(fp1,"%s",&b_list[i].b_price); 
		fscanf(fp1,"%s",&b_list[i].b_writer);
		fscanf(fp1,"%d",&b_list[i].b_quant);
	}
	for(i=0;i<100;i++)
	{
	    if(!strcmp(b_list[i].b_name,book_name))   //对比结构数组内的数据与该函数输入的数据 
		{
	    	temp=i;
		}
	}
	printf("书籍名称 书籍编号 书籍价格 书籍作者 书籍余量\n");
	if(!strcmp(b_list[temp].b_name,book_name))    //输出查询到的结果 
	{
		printf("%s\t  ",b_list[temp].b_name);
		printf("%s\t  ",b_list[temp].b_number);
		printf("%s\t  ",b_list[temp].b_price);
		printf("%s\t  ",b_list[temp].b_writer);
		printf("%d\t  ",b_list[temp].b_quant);
	}
	else printf("\n查询无结果\n");
	getchar();
	fclose(fp1);
	printf("\n是否继续查询？(是y / 否n)");
	order2=getchar();
	if(order2=='y'||order2=='Y')
	goto L2;
}

void borrow_book()//借阅图书
{
	char order3;
	int M=0;
	char book_name[100];
	L3:
	fp1=fopen("tushushujiku.txt","a+");
	
	for(i=0;i<100;i++)                        //将文档内的数据放入结构数组 
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
    import("请输入要借阅的书籍",book_name); 
    
    for(i=0;i<100;i++)                                //对比结构数组内的数据和该函数的输入数据 
	{
	    if(!strcmp(b_list[i].b_name,book_name))
		{
	    	temp=i;
		}
	}
	if(!strcmp(b_list[temp].b_name,book_name))          //输入学生信息或者输出查询结果 
	{
		if(b_list[temp].b_quant>0)
		{
		   import("学生姓名",r_list[0].r_name);
		   import("学生学号",r_list[0].r_number);
		   import("书籍名称",r_list[0].r_books);
	       b_list[temp].b_quant=b_list[temp].b_quant-1;
	       printf("借书成功\n");
		}
		else printf("书籍余量为零\n");
	}
	else
	printf("查询无结果\n");
	
	fp1=fopen("tushushujiku.txt","w");
	for(i=0;i<M;i++)                                   //将新的数据从结构数组存入文件中 
	{
	fprintf(fp1,"%s\t",b_list[i].b_name);
	fprintf(fp1,"%s\t",b_list[i].b_number);
	fprintf(fp1,"%s\t",b_list[i].b_price);
	fprintf(fp1,"%s\t",b_list[i].b_writer);
	fprintf(fp1,"%d\t",b_list[i].b_quant);
	fprintf(fp1,"\n");
    }
    fclose(fp1);
    
    fp2=fopen("jieshuqingkuang.txt","a+");                //将学生借书情况存入文件2 
	
	fprintf(fp2,"%s\t",r_list[0].r_name);
	fprintf(fp2,"%s\t",r_list[0].r_number);
	fprintf(fp2,"%s\t",r_list[0].r_books);
	fprintf(fp2,"\n");
	
	fclose(fp2);
	getchar();
    printf("是否继续借阅？(是y / 否n)");
	order3=getchar();
	if(order3=='y'||order3=='Y')
	goto L3;
}

void return_book()//还书 
{
	char order4;
	char book_name[100];
	char stu_name[100];
	int M=0;
	L4:
	fp1=fopen("tushushujiku.txt","a+");
	
	for(i=0;i<100;i++)                            //将文件内数据放入结构数组 
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
	
	import("请输入要还回的书籍",book_name);
	import("请输入学生姓名",stu_name);
	
	for(i=0;i<M;i++)                                  //对比、查询 
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
	else printf("查无此书\n");
	
	fp1=fopen("tushushujiku.txt","w");
	for(i=0;i<M;i++)                                   //将新的数据从结构数组存入文件中 
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
	for(j=0;j<100;j++)                                       //数据从 文件 →结构数组 
	{
		fscanf(fp2,"%s",&r_list[j].r_name);
		fscanf(fp2,"%s",&r_list[j].r_number);
		fscanf(fp2,"%s",&r_list[j].r_books);
	}
	fclose(fp2);
	
	for(j=0;j<100;j++)                                //对比、查询 
	{
	    if(strcmp(r_list[j].r_books,book_name)==0&&strcmp(r_list[j].r_name,stu_name)==0)
		{
	    	temp=j;
		}
	}
	
	fp3=fopen("huanshuqingkuang.txt","a+");
	if(strcmp(r_list[temp].r_books,book_name)==0&&strcmp(r_list[temp].r_name,stu_name)==0)            //输出查询结果 
		{
		fprintf(fp2,"%s\t%s\t%s\t已还\n",r_list[temp].r_name,r_list[temp].r_number,r_list[temp].r_books);//将还书情况录入文件3 
		printf("还书成功\n");
		}
	else printf("无借阅记录\n");
    fclose(fp3);
    
    getchar();
    printf("是否继续还书？(是y / 否n)");
	order4=getchar();
	if(order4=='y'||order4=='Y')
    goto L4;
}

void record_book()//查询学生借书记录
{ 
    i=0;
    int a[100];
    int order5;
    char s_name[100];
    
	fp2=fopen("jieshuqingkuang.txt","a+");
	for(j=0;j<100;j++)                                          //文件 →结构数组 
	{
		fscanf(fp2,"%s",&r_list[j].r_name);
		fscanf(fp2,"%s",&r_list[j].r_number);
		fscanf(fp2,"%s",&r_list[j].r_books);
	}
    L5:
	import("请输入学生姓名",s_name);
	
	for(j=0;j<100;j++)                                            //对比、查询 
	{
		if(!strcmp(r_list[j].r_name,s_name))
		{
			a[i]=j;
			i++;
		}
	}
    printf("\t\t\t\t学生姓名 学生学号 借书名称\n");
    for(j=0;j<i;j++)
    if(!strcmp(r_list[a[j]].r_name,s_name))
    printf("\t\t\t\t%s\t%s\t%s\n",r_list[a[j]].r_name,r_list[a[j]].r_number,r_list[a[j]].r_books);
	fclose(fp2);
	
    getchar();
	printf("是否继续查询？(是y / 否n)");
	order5=getchar();
	if(order5=='y'||order5=='Y')
	goto L5;
}  

void huanshu()//查询学生还书记录 
{
	i=0;
	int a[100];
    int order6;
    char s_name[100];
    
	fp3=fopen("huanshuqingkuang.txt","a+");
	for(j=0;j<100;j++)                                          //文件 →结构数组 
	{
		fscanf(fp3,"%s",&re_list[j].re_name);
		fscanf(fp3,"%s",&re_list[j].re_number);
		fscanf(fp3,"%s",&re_list[j].re_books);
		fscanf(fp3,"%s",&re_list[j].re_huan);
	}
    L6:
	import("请输入学生姓名",s_name);
	
	for(j=0;j<100;j++)                                            //对比、查询 
	{
		if(!strcmp(re_list[j].re_name,s_name))
		{
			a[i]=j;
			i++;
		}
	}
    printf("\n\t\t\t\t学生姓名 学生学号 借书名称\n");
    for(j=0;j<i;j++)
	if(!strcmp(re_list[a[j]].re_name,s_name))
    printf("\t\t\t\t%s\t%s\t%s\t%s\n",re_list[a[j]].re_name,re_list[a[j]].re_number,re_list[a[j]].re_books,re_list[a[j]].re_huan);
	fclose(fp3);
	
    getchar();
	printf("是否继续查询？(是y / 否n)");
	order6=getchar();
	if(order6=='y'||order6=='Y')
	goto L6;
}

void old_add()//原有书籍入库 
{
	char order7;
	int M=0;
	char book_name[100];
	int number;
	L7:
	fp1=fopen("tushushujiku.txt","a+");
	
	for(i=0;i<100;i++)                        //将文档内的数据放入结构数组 
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
    
	import("请输入要入库的书籍",book_name); 
    printf("\t\t请输入要增加的数量：");
	scanf("%d",&number); 
    
    for(i=0;i<100;i++)                                //对比结构数组内的数据和该函数的输入数据 
	{
	    if(!strcmp(b_list[i].b_name,book_name))
		{
	    	temp=i;
		}
	}
	if(!strcmp(b_list[temp].b_name,book_name))          //输入书籍信息或者输出查询结果 
	{
		b_list[temp].b_quant=b_list[temp].b_quant+number;
		printf("入库成功\n");
	}
	else printf("\t\t查无此书\n");
	
	fp1=fopen("tushushujiku.txt","w");
	for(i=0;i<M;i++)                                   //将新的数据从结构数组存入文件中 
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
    printf("是否继续录入书籍？(是y / 否n)");
	order7=getchar();
	if(order7=='y'||order7=='Y')
	goto L7;
}

int main()//主函数 
{
	system("color f1");//界面颜色
	show();
	
	return 0;
}
