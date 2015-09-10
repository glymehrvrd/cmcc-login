function [similarity, cost_rst] = bfmatcher(featureset1, featureset2)
    similarity=0;
    cost = zeros([length(featureset1), length(featureset2)]);
    for i=1:length(featureset1)
        for j=1:length(featureset2)
            cost(i,j)=sum(abs(featureset1(i,:)-featureset2(j,:)));
        end;
    end;
    cost_rst=cost;
    while ~isempty(cost)
        [val,row]=min(cost);
        [val,col]=min(val);
        row=row(col);
        if size(cost,1)~=1
            cost(row,:)=[];
        end;
        cost(:,col)=[];
        similarity=similarity+val;
    end;
end