package cn.itcast.hadoop.mr.llyy.enhance;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * 
 * 
 * ����ԭʼ��־���ݣ���ȡ���е�url����ѯ����⣬��ø�urlָ�����ҳ���ݵķ��������׷�ӵ�ԭʼ��־��
 * 
 * @author duanhaitao@itcast.cn
 * 
 */

// ����ԭʼ���� ��47���ֶΣ� ʱ��� ..... destip srcip ... url .. . get 200 ...
// ��ȡ���е�url��ѯ�����õ��ڶ������ʶ����Ϣ ��վ���Ƶ���������ʣ��ؼ��ʣ�ӰƬ�������ݣ����ݡ�������
// ���������׷�ӵ�ԭʼ��־����
// context.write( ʱ��� ..... destip srcip ... url .. . get 200 ...
// ��վ���Ƶ���������ʣ��ؼ��ʣ�ӰƬ�������ݣ����ݡ�������)
// ���ĳ��url�ڹ�����в鲻�������������������嵥
// context.write( url tocrawl)
public class LogEnhanceMapper extends
		Mapper<LongWritable, Text, Text, NullWritable> {

	private HashMap<String, String> ruleMap = new HashMap<>();

	// setup��������mapper task ��ʼ��ʱ������һ��
	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		DBLoader.dbLoader(ruleMap);
	}

	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

		String line = value.toString();

		String[] fields = StringUtils.split(line, "\t");
		try {
			if (fields.length > 27 && StringUtils.isNotEmpty(fields[26])
					&& fields[26].startsWith("http")) {
				String url = fields[26];
				String info = ruleMap.get(url);
				String result = "";
				if (info != null) {
					result = line + "\t" + info + "\n\r";
					context.write(new Text(result), NullWritable.get());
				} else {
					result = url + "\t" + "tocrawl" + "\n\r";
					context.write(new Text(result), NullWritable.get());
				}

			} else {
				return;
			}
		} catch (Exception e) {
			System.out.println("exception occured in mapper.....");
		}
	}

}
