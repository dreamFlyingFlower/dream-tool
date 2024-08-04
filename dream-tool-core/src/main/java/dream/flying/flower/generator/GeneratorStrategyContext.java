package dream.flying.flower.generator;

public class GeneratorStrategyContext {

	private String strategy = "SnowFlake";

	private int datacenterId;

	private int machineId;

	private SnowFlakeGenerator snowFlakeGenerator = new SnowFlakeGenerator(0, 0);

	private StringGenerator stringGenerator = new StringGenerator();

	public String generate() {
		if (strategy.equalsIgnoreCase("SnowFlake")) {
			return snowFlakeGenerator.nextId() + "";
		} else if (strategy.equalsIgnoreCase("uuid")) {
			return stringGenerator.uuidGenerate();
		} else {
			return stringGenerator.randomGenerate();
		}
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public int getDatacenterId() {
		return datacenterId;
	}

	public void setDatacenterId(int datacenterId) {
		this.datacenterId = datacenterId;
	}

	public int getMachineId() {
		return machineId;
	}

	public void setMachineId(int machineId) {
		this.machineId = machineId;
	}

	public SnowFlakeGenerator getSnowFlakeGenerator() {
		return snowFlakeGenerator;
	}

	public void setSnowFlakeGenerator(SnowFlakeGenerator snowFlakeGenerator) {
		this.snowFlakeGenerator = snowFlakeGenerator;
	}

	public StringGenerator getStringGenerator() {
		return stringGenerator;
	}

	public void setStringGenerator(StringGenerator stringGenerator) {
		this.stringGenerator = stringGenerator;
	}
}