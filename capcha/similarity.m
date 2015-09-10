files = dir('gray/digits/*.bmp');

rns=cell(10,1);

for i=1:10
    rns{i}=imread(['regular_numbers/' num2str(i-1) '.bmp']);
end;

sim=zeros(10,1);
for f=files'
    img=imread(['gray/digits/' f.name]);
    for i=1:10
        sim(i)=length(find((img|rns{i})==0));
        sprintf('similarity between %d and %s: %d', i-1, f.name, sim(i))
    end;
    [~,ind]=max(sim);
    sprintf('%s is most likely to be %d', f.name, ind-1)
end;