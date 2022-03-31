#include <iostream>
using namespace std;
#include <string.h> 
#define N 50      //�ñ���ϵͳ�е�����ַ��� ����Ҷ�����Ŀ 
#define M (2*N-1)     //���������е���������� 
#define MIN 10000.0  

FILE *fp1,*fp2,*fp3,*fp4,*fp5,*fp6;

class huffmantree{
private:
	typedef struct 
	{
		float weight;  //��¼Ȩ�� 
		int left,right,parent;  //��¼���������͸���� 
		char ch;  //��¼�ַ� 
	}Htnode,Huffmantree[M];
	
	typedef struct
	{
		char ch;  //��ŵ����ַ� 
		char bits[N];  //��ŵ����ַ��Ĺ��������� 
		int flag; //��Ǵӵڼ���λ�ÿ�ʼ��ȡ���������� 
	}Htcode,Huffmancode[N];
	//��Ź���������Ľṹ������
public:
	//�������������͹��������� 
void creat_huffmantree(Huffmantree tree,int n)  //��ʼ���������������� 
{
	getchar();
	int i,j;  //ѭ����ʹ�õı��� 
	float  min1,min2;  //����ҵ�����С������Ȩֵ 
	int node1,node2;  //�����СȨֵ��Ӧ�Ľ�� 
    int m=2*n-1;
	for(i=0;i<m;i++)  //��ʼ���������� 
	{
		tree[i].ch = '@';
		tree[i].left=-1;
		tree[i].right=-1;
		tree[i].parent=0;
		tree[i].weight=0.0;
	}
	
	printf("����������Ҷ�����ַ���Ȩֵ��\n");
	for(i=0;i<n;i++)  //¼��Ҷ�����Ϣ 
	{
		cout <<"�������"<<i+1<<"�������ַ���Ȩֵ���ÿո��������";
		cin >>tree[i].ch>>tree[i].weight;
		getchar();
	}
	
	//�ҵ���С������Ȩֵ��㣬�����µĽ�㣬������������ 
	for(i=n;i<m;i++)
	{
		min1=min2=10000.0;   //float��Ӧ�����ֵ
		node1=node2=-1;
		for(j=0;j<i;j++)
		{
			if(tree[j].weight<min1&&tree[j].parent==0)
			{
				min2=min1;  //�ı��СȨֵ 
				min1=tree[j].weight;  //�ı���СȨֵ 
				node2=node1;  //�ı��� 
				node1=j; 
			}
			else if(tree[j].weight<min2&&tree[j].parent==0)
			{
				min2=tree[j].weight;
				node2=j;
			}
		}
		tree[node1].parent=tree[node2].parent=i;
		tree[i].left=node1;  //�����µĽ�� 
		tree[i].right=node2;
		tree[i].weight=min1+min2;
		tree[i].parent=0; //���½����Ľ��ĸ��ڵ����� 
	} 
} 

void generate_code(Huffmantree tree,Huffmancode code,int n)  //������ű���Ľṹ�壬���ɱ��� 
{
	int i;  //ѭ����ʹ��
	int p;  //��Ϊ����ʹ�õĸ���� 
	int c;  //��Ϊ����ʹ�õĺ��ӽ�� 
	
	for(i=0;i<n;i++)
	{
		code[i].ch=tree[i].ch;  //����Ӧ�ַ�����code����� 
		code[i].flag=n;  //��־���ڿ��ܴ��ڵ����λ�� 
		
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
	cout <<"����������ṹ�彨�����"<<endl; 
}

int Levelwidth(Huffmantree tree,int k,int l)  //���ÿһ��Ŀ�� ,kΪ��ǰ���ڽ���λ�ã���m-1������㿪ʼ��lΪ������� 
{
	if(k==-1)	return 0;  //k=-1ʱ�����ǽ�㣬����0 
	else  //�ǽ�� 
	{
		if(l==0)	return 1;  //���������0ʱ��Ϊ��㱾������1 
		l=Levelwidth(tree,tree[k].left,l-1)+Levelwidth(tree,tree[k].right,l-1);  //������Ϊ0ʱ����δ�ݹ鵽����㣬�������ú��� 
	}
	return l; 
}

void printfhuffman(Huffmantree tree,Huffmancode code,int n)  //��ӡ�������� 
{
	int i,j,k;
	int m=2*n-1;
	
	fp1=fopen("hfmTree.txt","a+");
	
	for(j=0;j<n;j++)//���ļ��ڱ�����뷽ʽ 
	{
		fprintf(fp1,"%c ",code[j].ch);
		for(k=code[j].flag;k<n;k++)
			fprintf(fp1,"%c",code[j].bits[k]);
		fprintf(fp1," %d\n",code[j].flag);
	} 
	
	cout <<"�����Ĺ�������Ϊ��"<<endl;
	
	cout <<"  | tree[i].ch | tree[i].weight | tree[i].left | tree[i].right | tree[i].parent |"<<endl;
	for(i=0;i<m;i++)
	{
		cout <<i <<" "<<"|     "<<tree[i].ch<<"      |"<<"     "<<tree[i].weight<<"      |"<<"     "<<tree[i].left<<"      |"<<"     "<<tree[i].right<<"      |"<<"     "<<tree[i].parent<<"      |"<<endl;
	}
	
	fclose(fp1);
}

//����������� 
void Initialization(Huffmantree tree,Huffmancode code,int n)  //��ʼ����ӡ��������
{
	creat_huffmantree(tree,n);
	generate_code(tree,code,n);
	printfhuffman(tree,code,n);
}

void Encoding(Huffmancode code,int n)  //���� 
{
	getchar();
	char c;
	int i,j;
	char *p;
	char *q; 
	int tmp=0; 
	if(!code[0].flag)//����ڴ��в������뷽ʽ������ļ�hfmtree��ȡ 
	{
		fp1=fopen("hfmTree.txt","r");
		while(!feof(fp1)) //���ж�ȡ�ļ� 
		{
			char str[128]; 
        	if(fgets(str,128,fp1)!=NULL)
        	{
        		p=strtok(str," ");//���ո�ָ��ַ��� 
				code[tmp].ch=*p;

				p=strtok(NULL," ");//���ո�ָ��ַ���
				int x=strlen(p);
				
				q=strtok(NULL," ");//���ո�ָ��ַ���
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
	cout <<"������Ϊ��"; 
	while((c=fgetc(fp2))!='/')   //���ļ��ж�ȡ�����ַ� 
	{
		if(c==' ')  //��ȡ�ո񲢴�ӡ 
		{
			fprintf(fp3," ");
			cout <<" ";
		}
		else
		{
			for(i=0;i<n;i++)
			{
				if(c==code[i].ch)    //���� 
				{
					for(j=code[i].flag;j<n;j++)
					{
						fprintf(fp3,"%c",code[i].bits[j]);   //���ļ��д�ӡ����
						cout <<code[i].bits[j];   //���ն��д�ӡ����
					}
					break;
				}
			}
		}
	}
	fprintf(fp3,"/");  //���������ʶ��
	cout <<endl;   
	cout <<"�������"<<endl;
	fclose(fp2);
	fclose(fp3);
}

void Decoding(Huffmantree tree,int n)  //���� 
{
	char c;
	int i,j;
	int m=2*n-1;
	i=m-1;//iΪ����� 
	fp3=fopen("CodeFile.txt","a+");
	fp4=fopen("TextFile.txt","a+");
	
	while((c=fgetc(fp3))!='/')
	{
		if(c==' ')  //��ȡ�ո񲢴�ӡ
			fprintf(fp3," ");
		else
		{
			if(c=='0')   //�� 0 ������ 
				i=tree[i].left;
			else  //�� 1 ������ 
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
	fprintf(fp4,"/");  //���������ʶ�� 
	cout <<"�������"<<endl;
	fclose(fp3);
	fclose(fp4);
}

void Print()  //��ӡ�����ļ� 
{
	char c;
	int count=0;
	fp3=fopen("CodeFile.txt","a+");
	fp5=fopen("CodePrint.txt","a+");
	while((c=fgetc(fp3))!='/')  
	{
		if(c==' ')  //��ȡ�ո񲢴�ӡ
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
	fprintf(fp5,"/");  //���������ʶ�� 
	cout <<endl;
	cout <<"��ӡ����"<<endl;
	fclose(fp3);
	fclose(fp5);
}

void Tree_printing(Huffmantree tree,Huffmancode code,int n) //��ӡ�������� 
{
	int i=0;
	int m=2*n-1; 
	int a[m];//��Ž�� 
	int f=0;  //�ױ�ʶfront 
	int r=0;  //β��ʶrear 
	int j,t,num;  //j��tΪѭ������������num��¼��� 
	
	fp6=fopen("TreePrint.txt","a+");
	a[r++]=m-1;
	while(f!=r)
	{
		num=Levelwidth(tree,m-1,i);  //��ȡ��һ��Ŀ��
		for(j=n;j>i;j--)  //���ݲ�����ӡ�ո� 
		{
			fprintf(fp6,"   ");
			cout <<"   ";
		}
		
		for(t=0;t<num;t++)  //��ӡ���� 
		{
			if(tree[a[f]].left!=-1)  //����ý�㲻��Ҷ��� 
			{
				fprintf(fp6,"   %.2f   ",tree[a[f]].weight);  //��ӡȨֵ 
				cout <<tree[a[f]].weight;
				a[r++]=tree[a[f]].left;       //���ý��������������������� 
				a[r++]=tree[a[f]].right;
			}
			else  //��Ҷ��� 
			{
				fprintf(fp6,"   %c,%.2f   ",tree[a[f]].ch,tree[a[f]].weight);  //��ӡ�ַ���Ȩֵ 
				cout <<tree[a[f]].ch<<","<<tree[a[f]].weight<<" ";
			}
			f++;
		}
		
		fprintf(fp6,"\n");
		cout <<endl;
		i++;
	}
	cout <<"���������ʹ�ӡ����"<<endl;
	fclose(fp6);
} 

void menu()  //�˵����� 
{
	char order0,order;
	int n;
	cout << "������Ҷ���ĸ�����";
	cin >> n;
	getchar();
	Huffmantree tree;
	Huffmancode code;
	                                                                     
L0:	cout <<"\n\n\n\n\n\t\t* * * * * * * * * * * * * * * * * * *"<<endl;         
    cout <<"\t\t*           Huffmantree             *"<<endl;                   
    cout <<"\t\t*                                   *"<<endl;                    
	cout <<"\t\t*    ����������ѡ������Ҫ�Ĺ���     *"<<endl;                   
	cout <<"\t\t*     I.��ʼ��                      *"<<endl;                    
	cout <<"\t\t*     E.����                        *"<<endl;                    
	cout <<"\t\t*     D.����                        *"<<endl;                    
	cout <<"\t\t*     P.��ӡ�����ļ�                *"<<endl;
	cout <<"\t\t*     T.��ӡ��������                *"<<endl;
	cout <<"\t\t*     Q.��������ʹ��                *"<<endl;
	cout <<"\t\t* * * * * * * * * * * * * * * * * * *"<<endl; 
    cout <<"\t\t   ��ѡ������Ҫ�Ĺ��ܣ�";

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
    cout <<"�Ƿ����ʹ�ù���������ϵͳ��(��y / ��n)";
	order0=getchar();
	getchar();
	if(order0=='y'||order0=='Y')  
	{
		system("cls");goto L0;
	}
	
	L:
	system("cls");
	cout <<"\n\n\n\n\t\t\t\t��лʹ�ù���������ϵͳ"<<endl;
}
};

int main()//������ 
{
	system("color f1");//������ɫ
	huffmantree h;
	h.menu();
	
	return 0;
} 
