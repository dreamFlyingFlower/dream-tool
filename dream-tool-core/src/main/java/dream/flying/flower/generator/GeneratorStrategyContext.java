package dream.flying.flower.generator;

public class GeneratorStrategyContext {

	String strategy = "uuid";

	int datacenterId;

	int machineId;

	SnowFlakeGenerator snowFlakeGenerator = new SnowFlakeGenerator(0, 0);

	StringGenerator stringGenerator = new StringGenerator();

	public String generate() {
		if (strategy.equalsIgnoreCase("uuid")) {
			return stringGenerator.uuidGenerate();
		} else if (strategy.equalsIgnoreCase("SnowFlake")) {
			return snowFlakeGenerator.nextId() + "";
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