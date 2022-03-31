#include <iostream>
using namespace std;
#include <string.h> 
#define N 50      //该编码系统中的最大字符数 ，即叶结点数目 
#define M (2*N-1)     //哈夫曼树中的最大结点总数 
#define MIN 10000.0  

FILE *fp1,*fp2,*fp3,*fp4,*fp5,*fp6;

class huffmantree{
private:
	typedef struct 
	{
		float weight;  //记录权重 
		int left,right,parent;  //记录左右子树和父结点 
		char ch;  //记录字符 
	}Htnode,Huffmantree[M];
	
	typedef struct
	{
		char ch;  //存放单个字符 
		char bits[N];  //存放单个字符的哈夫曼编码 
		int flag; //标记从第几个位置开始读取哈夫曼编码 
	}Htcode,Huffmancode[N];
	//存放哈夫曼编码的结构体数组
public:
	//构建哈夫曼树和哈夫曼编码 
void creat_huffmantree(Huffmantree tree,int n)  //初始化，建立哈夫曼树 
{
	getchar();
	int i,j;  //循环内使用的变量 
	float  min1,min2;  //存放找到的最小的两个权值 
	int node1,node2;  //存放最小权值对应的结点 
    int m=2*n-1;
	for(i=0;i<m;i++)  //初始化哈夫曼树 
	{
		tree[i].ch = '@';
		tree[i].left=-1;
		tree[i].right=-1;
		tree[i].parent=0;
		tree[i].weight=0.0;
	}
	
	printf("请依次输入叶结点的字符和权值：\n");
	for(i=0;i<n;i++)  //录入叶结点信息 
	{
		cout <<"请输入第"<<i+1<<"个结点的字符和权值（用空格隔开）：";
		cin >>tree[i].ch>>tree[i].weight;
		getchar();
	}
	
	//找到最小的两个权值结点，构造新的结点，建立哈夫曼树 
	for(i=n;i<m;i++)
	{
		min1=min2=10000.0;   //float对应的最大值
		node1=node2=-1;
		for(j=0;j<i;j++)
		{
			if(tree[j].weight<min1&&tree[j].parent==0)
			{
				min2=min1;  //改变次小权值 
				min1=tree[j].weight;  //改变最小权值 
				node2=node1;  //改变结点 
				node1=j; 
			}
			else if(tree[j].weight<min2&&tree[j].parent==0)
			{
				min2=tree[j].weight;
				node2=j;
			}
		}
		tree[node1].parent=tree[node2].parent=i;
		tree[i].left=node1;  //建立新的结点 
		tree[i].right=node2;
		tree[i].weight=min1+min2;
		tree[i].parent=0; //将新建立的结点的父节点置零 
	} 
} 

void generate_code(Huffmantree tree,Huffmancode code,int n)  //建立存放编码的结构体，生成编码 
{
	int i;  //循环内使用
	int p;  //作为过渡使用的父结点 
	int c;  //作为过渡使用的孩子结点 
	
	for(i=0;i<n;i++)
	{
		code[i].ch=tree[i].ch;  //将对应字符存入code结点中 
		code[i].flag=n;  //标志置于可能存在的最后位置 
		
		c=i;  
		p=tree[c].parent;
		while(p)
		{
			code[i].flag--;
			if(tree[p].left==c)
				code[i].bits[code[i].flag]='0';
			else
				code[i].bits[code[i].flag]='1';
			c=p;
			p=tree[p].parent;
		} 
	}
	cout <<"哈夫曼编码结构体建立完毕"<<endl; 
}

int Levelwidth(Huffmantree tree,int k,int l)  //求出每一层的宽度 ,k为当前所在结点的位置，从m-1即根结点开始，l为所求层数 
{
	if(k==-1)	return 0;  //k=-1时，不是结点，返回0 
	else  //是结点 
	{
		if(l==0)	return 1;  //当层数变成0时，为结点本身，返回1 
		l=Levelwidth(tree,tree[k].left,l-1)+Levelwidth(tree,tree[k].right,l-1);  //层数不为0时，还未递归到所求层，继续调用函数 
	}
	return l; 
}

void printfhuffman(Huffmantree tree,Huffmancode code,int n)  //打印哈夫曼树 
{
	int i,j,k;
	int m=2*n-1;
	
	fp1=fopen("hfmTree.txt","a+");
	
	for(j=0;j<n;j++)//在文件内保存编码方式 
	{
		fprintf(fp1,"%c ",code[j].ch);
		for(k=code[j].flag;k<n;k++)
			fprintf(fp1,"%c",code[j].bits[k]);
		fprintf(fp1," %d\n",code[j].flag);
	} 
	
	cout <<"构建的哈夫曼树为："<<endl;
	
	cout <<"  | tree[i].ch | tree[i].weight | tree[i].left | tree[i].right | tree[i].parent |"<<endl;
	for(i=0;i<m;i++)
	{
		cout <<i <<" "<<"|     "<<tree[i].ch<<"      |"<<"     "<<tree[i].weight<<"      |"<<"     "<<tree[i].left<<"      |"<<"     "<<tree[i].right<<"      |"<<"     "<<tree[i].parent<<"      |"<<endl;
	}
	
	fclose(fp1);
}

//五个操作函数 
void Initialization(Huffmantree tree,Huffmancode code,int n)  //初始化打印哈夫曼树
{
	creat_huffmantree(tree,n);
	generate_code(tree,code,n);
	printfhuffman(tree,code,n);
}

void Encoding(Huffmancode code,int n)  //编码 
{
	getchar();
	char c;
	int i,j;
	char *p;
	char *q; 
	int tmp=0; 
	if(!code[0].flag)//如果内存中不含编码方式，则从文件hfmtree读取 
	{
		fp1=fopen("hfmTree.txt","r");
		while(!feof(fp1)) //按行读取文件 
		{
			char str[128]; 
        	if(fgets(str,128,fp1)!=NULL)
        	{
        		p=strtok(str," ");//按空格分隔字符串 
				code[tmp].ch=*p;

				p=strtok(NULL," ");//按空格分隔字符串
				int x=strlen(p);
				
				q=strtok(NULL," ");//按空格分隔字符串
				char sum=*q;
				code[tmp].flag=sum-'0';
				
				int m=code[tmp].flag;
				while(x>0)
				{
					code[tmp].bits[m++]=*(p++);

					x--;
				}
			}
			tmp++;
    	}
    	fclose(fp1);
	}
	
	fp2=fopen("ToBeTran.txt","a+");
	fp3=fopen("CodeFile.txt","a+");
	cout <<"编码结果为："; 
	while((c=fgetc(fp2))!='/')   //从文件中读取单个字符 
	{
		if(c==' ')  //读取空格并打印 
		{
			fprintf(fp3," ");
			cout <<" ";
		}
		else
		{
			for(i=0;i<n;i++)
			{
				if(c==code[i].ch)    //编码 
				{
					for(j=code[i].flag;j<n;j++)
					{
						fprintf(fp3,"%c",code[i].bits[j]);   //在文件中打印编码
						cout <<code[i].bits[j];   //在终端中打印编码
					}
					break;
				}
			}
		}
	}
	fprintf(fp3,"/");  //输入结束标识符
	cout <<endl;   
	cout <<"编码结束"<<endl;
	fclose(fp2);
	fclose(fp3);
}

void Decoding(Huffmantree tree,int n)  //译码 
{
	char c;
	int i,j;
	int m=2*n-1;
	i=m-1;//i为根结点 
	fp3=fopen("CodeFile.txt","a+");
	fp4=fopen("TextFile.txt","a+");
	
	while((c=fgetc(fp3))!='/')
	{
		if(c==' ')  //读取空格并打印
			fprintf(fp3," ");
		else
		{
			if(c=='0')   //是 0 ，向左 
				i=tree[i].left;
			else  //是 1 ，向右 
				i=tree[i].right;
			if(tree[i].left==-1)
			{
				fprintf(fp4,"%c",tree[i].ch);
				cout <<tree[i].ch;
				i=m-1;
			}
		}
	} 
	cout <<endl;
	fprintf(fp4,"/");  //输入结束标识符 
	cout <<"译码结束"<<endl;
	fclose(fp3);
	fclose(fp4);
}

void Print()  //打印代码文件 
{
	char c;
	int count=0;
	fp3=fopen("CodeFile.txt","a+");
	fp5=fopen("CodePrint.txt","a+");
	while((c=fgetc(fp3))!='/')  
	{
		if(c==' ')  //读取空格并打印
			fprintf(fp3," ");
		else
		{
			fprintf(fp5,"%c",c);
			cout <<c;
			count++;
			if(count%50==0)
			cout <<endl;
		}
	}
	fprintf(fp5,"/");  //输入结束标识符 
	cout <<endl;
	cout <<"打印结束"<<endl;
	fclose(fp3);
	fclose(fp5);
}

void Tree_printing(Huffmantree tree,Huffmancode code,int n) //打印哈夫曼树 
{
	int i=0;
	int m=2*n-1; 
	int a[m];//存放结点 
	int f=0;  //首标识front 
	int r=0;  //尾标识rear 
	int j,t,num;  //j，t为循环计数变量，num记录宽度 
	
	fp6=fopen("TreePrint.txt","a+");
	a[r++]=m-1;
	while(f!=r)
	{
		num=Levelwidth(tree,m-1,i);  //获取这一层的宽度
		for(j=n;j>i;j--)  //根据层数打印空格 
		{
			fprintf(fp6,"   ");
			cout <<"   ";
		}
		
		for(t=0;t<num;t++)  //打印树型 
		{
			if(tree[a[f]].left!=-1)  //如果该结点不是叶结点 
			{
				fprintf(fp6,"   %.2f   ",tree[a[f]].weight);  //打印权值 
				cout <<tree[a[f]].weight;
				a[r++]=tree[a[f]].left;       //将该结点的左右子树存入数组中 
				a[r++]=tree[a[f]].right;
			}
			else  //是叶结点 
			{
				fprintf(fp6,"   %c,%.2f   ",tree[a[f]].ch,tree[a[f]].weight);  //打印字符和权值 
				cout <<tree[a[f]].ch<<","<<tree[a[f]].weight<<" ";
			}
			f++;
		}
		
		fprintf(fp6,"\n");
		cout <<endl;
		i++;
	}
	cout <<"哈夫曼树型打印结束"<<endl;
	fclose(fp6);
} 

void menu()  //菜单函数 
{
	char order0,order;
	int n;
	cout << "请输入叶结点的个数：";
	cin >> n;
	getchar();
	Huffmantree tree;
	Huffmancode code;
	                                                                     
L0:	cout <<"\n\n\n\n\n\t\t* * * * * * * * * * * * * * * * * * *"<<endl;         
    cout <<"\t\t*           Huffmantree             *"<<endl;                   
    cout <<"\t\t*                                   *"<<endl;                    
	cout <<"\t\t*    请输入数字选择所需要的功能     *"<<endl;                   
	cout <<"\t\t*     I.初始化                      *"<<endl;                    
	cout <<"\t\t*     E.编码                        *"<<endl;                    
	cout <<"\t\t*     D.译码                        *"<<endl;                    
	cout <<"\t\t*     P.打印代码文件                *"<<endl;
	cout <<"\t\t*     T.打印哈夫曼树                *"<<endl;
	cout <<"\t\t*     Q.结束本次使用                *"<<endl;
	cout <<"\t\t* * * * * * * * * * * * * * * * * * *"<<endl; 
    cout <<"\t\t   请选择您需要的功能：";

	order=getchar();
	
	switch(order)
	{
		case 'I':Initialization(tree,code,n);   break;
		case 'E':Encoding(code,n);              break;
		case 'D':Decoding(tree,n);              break;
		case 'P':Print();		                break;
		case 'T':Tree_printing(tree,code,n);	break;
		case 'Q':goto L;
		default :cout <<"\n\t\t\tWronging order"<<endl;
	}
    getchar();
    cout <<"是否继续使用哈夫曼编码系统？(是y / 否n)";
	order0=getchar();
	getchar();
	if(order0=='y'||order0=='Y')  
	{
		system("cls");goto L0;
	}
	
	L:
	system("cls");
	cout <<"\n\n\n\n\t\t\t\t感谢使用哈夫曼编码系统"<<endl;
}
};

int main()//主函数 
{
	system("color f1");//界面颜色
	huffmantree h;
	h.menu();
	
	return 0;
} 
